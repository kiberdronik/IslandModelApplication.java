package ru.javarush.islandModel.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.javarush.islandModel.utils.RandomService;
import ru.javarush.islandModel.model.Plant;

import ru.javarush.islandModel.utils.Statistics;
import ru.javarush.islandModel.model.animal.Animal;
import ru.javarush.islandModel.model.animal.herbivore.*;
import ru.javarush.islandModel.model.animal.predator.*;
import ru.javarush.islandModel.model.island.Coordinate;
import ru.javarush.islandModel.model.island.Location;
import ru.javarush.islandModel.repository.LocationRepository;
import ru.javarush.islandModel.settings.Settings;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Service
@Slf4j
public class AnimalService {
    private final LocationRepository locationRepository;
    private static final Map<Class<? extends Animal>, Double> DAILY_ALLOWANCE;

    static {
        DAILY_ALLOWANCE = Settings.getSettings().getDailyAllowance();
    }

    public AnimalService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void move(Animal animal) {
        if (animal instanceof Caterpillar) return;
        int randomCountOfSteps = RandomService.getRandomCountOfSteps(animal.getClass());

        List<Coordinate> prev = new ArrayList<>();
        int count = 0;

        while (count < randomCountOfSteps) {
            List<Coordinate> available = locationRepository.getNearestAvailableCoordinates(animal);
            Coordinate current = animal.getCurrentCoordinate();
            if (!prev.isEmpty()) available.removeAll(prev);
            if (available.isEmpty()) break;
            takeStep(animal, available);
            prev.add(current);
            ++count;
        }
        if (animal.isExhausted()) {
            Statistics.addDiedOfHunger(animal.getClass());
            delete(animal);
        }
    }

    public void eat(Animal animal) {
        Lock lock = locationRepository.getLocationByCoordinate(animal.getCurrentCoordinate()).getLock();
        boolean isLockAcquired = false;
        try {
            isLockAcquired = lock.tryLock(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.info("{} не удалось поесть в месте с координатами {}", animal, animal.getCurrentCoordinate());
        }
        if(isLockAcquired) {
            try {
                if (animal instanceof Predator) {
                    List<Animal> potentialVictims = getPotentialVictims(animal);
                    if (!potentialVictims.isEmpty()) hunt(animal, potentialVictims);
                    else animal.starve();
                }
                if (animal instanceof Herbivore && animal.isHungry()) {
                    eatPlant(animal);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public void reproduce(Animal animal) {
        Location current = locationRepository.getLocationByCoordinate(animal.getCurrentCoordinate());

        Lock lock = current.getLock();
        boolean isLockAcquired = false;
        try {
            isLockAcquired = lock.tryLock(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.info("попытка разведения {} в месте с координатами {} не удалась", animal, current.getCoordinate());
        }
        if(isLockAcquired) {
            try {
                if (current.isItPossibleToAddAnimal(animal.getClass()) && current.isReproductionPossible(animal)) {
                    List<Animal> brood = RandomService.getBroodRandomCount(animal.getClass(), current);
                    current.addBrood(animal.getClass(), brood);
                    Statistics.addWasBorn(animal.getClass(), brood.size());
                }
            } finally {
                lock.unlock();
            }
        }
    }

    public void delete(Animal animal) {
        Location current = locationRepository.getLocationByCoordinate(animal.getCurrentCoordinate());

        Lock lock = current.getLock();
        boolean isLockAcquired = false;
        try {
            isLockAcquired = lock.tryLock(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.info("неудачная попытка удалить {} в месте с координатами {}", animal,
                    current.getCoordinate());
        }
        if(isLockAcquired) {
            try {
                current.removeAnimal(animal);
                animal = null;
            } finally {
                lock.unlock();
            }
        }
    }

    private List<Animal> getPotentialVictims(Animal animal) {
        Location current = locationRepository.getLocationByCoordinate(animal.getCurrentCoordinate());
        return current.getEatablesOfCertainTypes(Settings.getSettings().getFoodPreferences().get(animal.getClass()).keySet());
    }

    private void takeStep(Animal animal, List<Coordinate> available) {
        Location current = locationRepository.getLocationByCoordinate(animal.getCurrentCoordinate());
        Location next = locationRepository.getLocationByCoordinate(RandomService.chooseRandomCoordinate(available));
        Lock lockCurrent = current.getLock();
        Lock lockNext = next.getLock();
        boolean isLockCurrentAcquired = false;
        boolean isLockNextAcquired = false;
        try {
            isLockCurrentAcquired = lockCurrent.tryLock(1, TimeUnit.SECONDS);
            isLockNextAcquired = lockNext.tryLock(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.info("Движение животного {} из {} в {} было прервано", animal, current.getCoordinate(),
                    next.getCoordinate());
        }
        if(isLockCurrentAcquired && isLockNextAcquired) {
            try {
                current.removeAnimal(animal);
                next.addAnimal(animal);
            } finally {
                lockCurrent.unlock();
                lockNext.unlock();
            }
            animal.setCurrentCoordinate(next.getCoordinate());
            animal.starve();
        }
    }

    private void hunt(Animal animal, List<Animal> localPotentialVictims) {
        for (Animal localPotentialVictim : localPotentialVictims) {
            if (!animal.isHungry()) break;
            if (RandomService.haveEaten((Predator) animal, localPotentialVictim)) {
                double victimWeight = localPotentialVictim.getCurrentWeight();
                double currentSaturationWithFood = animal.getSaturationWithFood() + victimWeight;
                delete(localPotentialVictim);
                animal.setCurrentWeight(animal.getCurrentWeight() + victimWeight);
                animal.setSaturationWithFood(Math.min(currentSaturationWithFood, DAILY_ALLOWANCE.get(animal.getClass())));
                Statistics.addEatenAnimal(localPotentialVictim.getClass());
            } else animal.starve();
        }
    }

    private void eatPlant(Animal animal) {
        Location current = locationRepository.getLocationByCoordinate(animal.getCurrentCoordinate());
        Plant plant = current.getPlant();

        if (plant.getCurrentWeight() <= 0.5) {
            animal.starve();
            return;
        }
        if (animal instanceof Caterpillar) {
            plant.setCurrentWeight(plant.getCurrentWeight() - DAILY_ALLOWANCE.get(Caterpillar.class));
            animal.setSaturationWithFood(DAILY_ALLOWANCE.get(Caterpillar.class));
            return;
        }

        double howMuchToEat = DAILY_ALLOWANCE.get(animal.getClass()) - animal.getSaturationWithFood();
        if (plant.getCurrentWeight() < howMuchToEat) howMuchToEat = plant.getCurrentWeight();

        animal.setCurrentWeight(animal.getCurrentWeight() + howMuchToEat);
        plant.setCurrentWeight(plant.getCurrentWeight() - howMuchToEat);
        animal.setSaturationWithFood(animal.getSaturationWithFood() + howMuchToEat);
    }
}
