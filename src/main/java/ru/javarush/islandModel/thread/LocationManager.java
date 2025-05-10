package ru.javarush.islandModel.thread;

import ru.javarush.islandModel.model.animal.Animal;
import ru.javarush.islandModel.model.island.Location;
import ru.javarush.islandModel.service.AnimalService;
import ru.javarush.islandModel.service.LocationService;
import ru.javarush.islandModel.thread.tasks.AnimalLocalTask;
import ru.javarush.islandModel.thread.tasks.LocalTask;
import ru.javarush.islandModel.thread.tasks.PlantLocalTask;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LocationManager implements Runnable {
    private final LocationService locationService;
    private final AnimalService animalService;
    private final Queue<LocalTask> tasks = new ConcurrentLinkedQueue<>();

    public LocationManager(LocationService locationService, AnimalService animalService) {
        this.locationService = locationService;
        this.animalService = animalService;
    }

    @Override
    public void run() {
        locationService.getLocations().stream()
                .filter(location -> !location.isRiver())
                .forEach(this::setLocalTask);
    }

    private void setLocalTask(Location location) {
        List<Animal> localAnimals = location.getAllAnimals();

        location.getLock().lock();
        try {
            localAnimals.stream()
                    .filter(Objects::nonNull)
                    .forEach(animal -> tasks.add(new AnimalLocalTask(animal, animalService)));
            tasks.add(new PlantLocalTask(location.getPlant()));
        } finally {
            location.getLock().unlock();
        }

        tasks.forEach(LocalTask::execute);
        tasks.clear();
    }
}
