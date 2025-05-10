package ru.javarush.islandModel.utils;

import ru.javarush.islandModel.model.animal.Animal;
import ru.javarush.islandModel.model.animal.AnimalFactory;
import ru.javarush.islandModel.model.animal.predator.Predator;
import ru.javarush.islandModel.model.island.Coordinate;
import ru.javarush.islandModel.model.island.Location;
import ru.javarush.islandModel.settings.Settings;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RandomService {
    private static final Map<Class<? extends Animal>, Integer> MAX_COUNT_OF_BROOD;
    private static final Map<Class<? extends Animal>, Integer> MAX_COUNT_ON_LOCATION;
    private static final Map<Class<? extends Animal>, Integer> MAX_COUNT_OF_STEPS;
    private static final Map<Class<? extends Predator>, Map<Class<? extends Animal>, Integer>> FOOD_PREFERENCES;

    static {
        MAX_COUNT_OF_STEPS = Settings.getSettings().getMaxCountOfSteps();
        FOOD_PREFERENCES = Settings.getSettings().getFoodPreferences();
        MAX_COUNT_OF_BROOD = Settings.getSettings().getMaxCountOfBrood();
        MAX_COUNT_ON_LOCATION = Settings.getSettings().getMaxCountOnLocation();
    }

    public static Map<Class<? extends Animal>, List<Animal>> getAnimalsRandomCountOnLocation(Coordinate coordinate) {
        return MAX_COUNT_ON_LOCATION.entrySet().stream()
                .map(entry -> setRandomCount(entry, coordinate))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static int getRandomCountOfSteps(Class<? extends Animal> clazz) {
        return ThreadLocalRandom.current().nextInt(MAX_COUNT_OF_STEPS.get(clazz) + 1);
    }

    public static Coordinate chooseRandomCoordinate(List<Coordinate> available) {
        return available.get(ThreadLocalRandom.current().nextInt(available.size()));
    }

    public static boolean haveEaten(Predator predator, Animal eatable) {
        return ThreadLocalRandom.current().nextInt(100) + 1
                <= FOOD_PREFERENCES.get(predator.getClass()).get(eatable.getClass());
    }

    public static List<Animal> getBroodRandomCount(Class<? extends Animal> clazz, Location location) {
        int howManyCertainTypeCanBeAdded = MAX_COUNT_ON_LOCATION.get(clazz) - location.getCountOfCertainType(clazz);
        int resultCount = ThreadLocalRandom.current().nextInt(Math.min(howManyCertainTypeCanBeAdded,
                MAX_COUNT_OF_BROOD.get(clazz)) + 1);
        return AnimalFactory.getCertainNumberOfAnimals(clazz, resultCount, location.getCoordinate());
    }

    private static Map.Entry<Class<? extends Animal>, List<Animal>> setRandomCount(Map.Entry<Class<? extends Animal>,
            Integer> entry, Coordinate coordinate) {
        int count = ThreadLocalRandom.current().nextInt(entry.getValue() + 1);
        return Map.entry(entry.getKey(), AnimalFactory.getCertainNumberOfAnimals(entry.getKey(), count, coordinate));
    }
}
