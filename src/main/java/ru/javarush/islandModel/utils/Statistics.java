package ru.javarush.islandModel.utils;

import lombok.Getter;
import ru.javarush.islandModel.model.AnimalTypeInfo;
import ru.javarush.islandModel.model.Plant;
import ru.javarush.islandModel.model.animal.Animal;
import ru.javarush.islandModel.model.island.Location;
import ru.javarush.islandModel.service.LocationService;
import ru.javarush.islandModel.settings.Settings;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class Statistics {
    private final LocationService locationService;
    private static Map<Class<? extends Animal>, Long> wasEatenFromStart = new ConcurrentHashMap<>();
    private static Map<Class<? extends Animal>, Long> wasEatenForLastCycle = new ConcurrentHashMap<>();
    private static Map<Class<? extends Animal>, Long> diedOfHungerFromStart = new ConcurrentHashMap<>();
    private static Map<Class<? extends Animal>, Long> diedOfHungerForLastCycle = new ConcurrentHashMap<>();
    private static Map<Class<? extends Animal>, Long> wasBornFromStart = new ConcurrentHashMap<>();
    private static Map<Class<? extends Animal>, Long> wasBornForLastCycle = new ConcurrentHashMap<>();
    private static Map<Class<? extends Animal>, Long> initialNumberOfAnimals = new ConcurrentHashMap<>();
    private static Map<Class<? extends Animal>, Long> lastNumberOfAnimals = new ConcurrentHashMap<>();
    private static Map<Class<? extends Animal>, Long> extinctTypeOfAnimals = new ConcurrentHashMap<>();
    private static double initialPlantWeight;
    private static double lastPlantWeight;
    private final static Map<Class<? extends Animal>, String> ANIMAL_PRINTS = Settings.getSettings().getAnimalPrints();
    private final static String PLANT_PRINT = Settings.getSettings().getPlantPrint();
    private static long countOfCycles;
    private static boolean haveCriticalNumberOfAnimalsDied;

    public Statistics(LocationService locationService) {
        this.locationService = locationService;
    }

    public void printStatistics() {
        System.out.println("День " + ++countOfCycles);
        ANIMAL_PRINTS.forEach((clazz, print) -> {
            AnimalTypeInfo info = getAnimalTypeInfo(clazz, print);
            System.out.println(info);
            if (info.getTotalCount() == 0 && !extinctTypeOfAnimals.containsKey(clazz)) {
                extinctTypeOfAnimals.put(clazz, countOfCycles);
            }
            if (extinctTypeOfAnimals.size() >= Settings.getSettings().getCriticalNumberExtinctTypes()
                    || extinctTypeOfAnimals.size() == ANIMAL_PRINTS.size())
                haveCriticalNumberOfAnimalsDied = true;
        });
        System.out.println(getPlantInfo() + "\n");

        printAnimalsChanges(StateOfAnimal.BE_EATEN);
        System.out.println("___");
        printAnimalsChanges(StateOfAnimal.DIED_OF_HUNGER);
        System.out.println("___");
        printAnimalsChanges(StateOfAnimal.BE_BORN);

        if (Settings.getSettings().isGetOnLocationStatistics()) printLocationStatistics();

        clearPrevCycle();
        System.out.println("\n__________________________________________________________________________");
    }

    public static void addEatenAnimal(Class<? extends Animal> clazz) {
        wasEatenFromStart.put(clazz, wasEatenFromStart.containsKey(clazz) ? wasEatenFromStart.get(clazz) + 1 : 1);
        wasEatenForLastCycle.put(clazz, wasEatenForLastCycle.containsKey(clazz) ? wasEatenForLastCycle.get(clazz) + 1
                : 1);
    }

    public static void addDiedOfHunger(Class<? extends Animal> clazz) {
        diedOfHungerFromStart.put(clazz, diedOfHungerFromStart.containsKey(clazz) ?
                diedOfHungerFromStart.get(clazz) + 1 : 1);
        diedOfHungerForLastCycle.put(clazz, diedOfHungerForLastCycle.containsKey(clazz) ?
                diedOfHungerForLastCycle.get(clazz) + 1 : 1);
    }

    public static void addWasBorn(Class<? extends Animal> clazz, int count) {
        wasBornFromStart.put(clazz, wasBornFromStart.containsKey(clazz) ? wasBornFromStart.get(clazz) + count : 1);
        wasBornForLastCycle.put(clazz, wasBornForLastCycle.containsKey(clazz) ?
                wasBornForLastCycle.get(clazz) + count : 1);
    }

    public void printStatisticsAfterGameOver() {
        System.out.printf("Игра длилась %s дней. За это время произошли следующие изменения: \n" +
                "первоначальное процентное изменение последнего дня \n", countOfCycles);
        ANIMAL_PRINTS.forEach((clazz, print) -> {
            printFinalResult(initialNumberOfAnimals.get(clazz), lastNumberOfAnimals.get(clazz), print);
        });
        printFinalResult((int)initialPlantWeight, (int)lastPlantWeight, PLANT_PRINT);
        extinctTypeOfAnimals.forEach((clazz, day) ->
                System.out.printf("Вид \"%s\" исчез в день %s ", ANIMAL_PRINTS.get(clazz), day));
    }

    public static boolean isHaveCriticalNumberOfAnimalsDied() {
        return haveCriticalNumberOfAnimalsDied;
    }

    private static void clearPrevCycle() {
        wasEatenForLastCycle.clear();
        diedOfHungerForLastCycle.clear();
        wasBornForLastCycle.clear();
    }

    private void printAnimalsChanges(StateOfAnimal state) {
        Map<Class<? extends Animal>, Long> fromStart = getDataFromStart(state);
        Map<Class<? extends Animal>, Long> forLastCycle = getDataForLastCycle(state);

        AtomicBoolean areThereAny = new AtomicBoolean(false);
        StringBuilder infoFromStart = new StringBuilder();

        fromStart.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .forEach(entry -> {
                    infoFromStart.append(String.format("%s %s ", ANIMAL_PRINTS.get(entry.getKey()), entry.getValue()));
                    areThereAny.set(true);
                });

        if (!areThereAny.get()) System.out.printf("С начала игры ни одно животное не %s", state.singular);
        else System.out.printf("С начала игры %s:" + infoFromStart.append("\n"), state.plural);

        areThereAny.set(false);
        StringBuilder infoLastTime = new StringBuilder();

        forLastCycle.entrySet().stream()
                .filter(entry -> entry.getValue() > 0)
                .forEach(entry -> {
                    infoLastTime.append(String.format("%s %s ", ANIMAL_PRINTS.get(entry.getKey()), entry.getValue()));
                    areThereAny.set(true);
                });

        if (!areThereAny.get()) System.out.printf("За последнее время ни одно животное не %s", state.singular);
        else System.out.printf("За последний день %s:" + infoLastTime.append("\n"), state.plural);
    }

    private Map<Class<? extends Animal>, Long> getDataFromStart(StateOfAnimal state) {
        return switch (state.name()) {
            case "BE_EATEN" -> wasEatenFromStart;
            case "DIED_OF_HUNGER" -> diedOfHungerFromStart;
            default -> wasBornFromStart;
        };
    }

    private Map<Class<? extends Animal>, Long> getDataForLastCycle(StateOfAnimal state) {
        return switch (state.name()) {
            case "BE_EATEN" -> wasEatenForLastCycle;
            case "DIED_OF_HUNGER" -> diedOfHungerForLastCycle;
            default -> wasBornForLastCycle;
        };
    }

    private void printLocationStatistics() {
        System.out.println("___ Статистика по каждому местоположению:\n" +
                " ___ ");
        locationService.getLocations()
                .forEach(System.out::println);
    }

    private AnimalTypeInfo getAnimalTypeInfo(Class<? extends Animal> clazz, String print) {
        AtomicReference<Double> sumSaturationWithFood = new AtomicReference<>(0.0);
        AtomicInteger maleCount = new AtomicInteger(0);
        AtomicInteger femaleCount = new AtomicInteger(0);

        int totalCount = (int) locationService.getLocations().stream()
                .filter(location -> !location.isRiver())
                .map(Location::getAnimals)
                .map(map -> map.get(clazz))
                .flatMap(Collection::stream)
                .peek(animal -> sumSaturationWithFood.getAndUpdate(v -> v + animal.getSaturationWithFood()))
                .peek(animal -> {
                    if(animal.isMale()) maleCount.addAndGet(1);
                    else femaleCount.addAndGet(1);
                })
                .count();

        saveAnimalTypeInfo(clazz, totalCount);

        return AnimalTypeInfo.builder()
                .totalCount(totalCount)
                .male(maleCount.get())
                .female(femaleCount.get())
                .averageSaturationWithFood(totalCount == 0? 0 : (sumSaturationWithFood.get()/totalCount * 100)
                        / Settings.getSettings().getDailyAllowance().get(clazz))
                .print(print)
                .build();
    }

    private String getPlantInfo() {
        double totalPlantWeight = locationService.getLocations().stream()
                .filter(location -> !location.isRiver())
                .map(Location::getPlant)
                .mapToDouble(Plant::getCurrentWeight)
                .sum();

        if (initialPlantWeight == 0.0) initialPlantWeight = totalPlantWeight;
        else lastPlantWeight = totalPlantWeight;

        long countOfLocations = locationService.getLocations().stream().filter(location -> !location.isRiver()).count();

        return PLANT_PRINT + " средний вес на месте " + totalPlantWeight/countOfLocations;
    }

    private void saveAnimalTypeInfo(Class<? extends Animal> clazz, long count) {
        if (!initialNumberOfAnimals.containsKey(clazz)) initialNumberOfAnimals.put(clazz, count);
        else lastNumberOfAnimals.put(clazz, count);
    }

    private void printFinalResult(long initialCount, long countLastDay, String print) {
        String increaseOrDecrease = initialCount > countLastDay ? "↓ вниз" : "↑ вверх";
        double percentageChange = initialCount > countLastDay? (double) (initialCount - countLastDay) / initialCount * 100 :
                (double) (countLastDay - initialCount) / initialCount * 100;
        System.out.printf("%s %-8s %-12s %-6s %.1f%% \n", print, initialCount, countLastDay, increaseOrDecrease,
                percentageChange);
    }

    @Getter
    public enum StateOfAnimal {
        BE_EATEN("было съедено.\n", "были съедены. "),
        DIED_OF_HUNGER("умерло от голода.\n", "умерли от голода. "),
        BE_BORN("родилось.\n", "родились. ");

        private final String singular;
        private final String plural;

        StateOfAnimal(String singular, String plural) {
            this.singular = singular;
            this.plural = plural;
        }
    }
}
