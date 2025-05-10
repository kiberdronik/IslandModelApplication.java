package ru.javarush.islandModel.settings;

import ru.javarush.islandModel.model.animal.*;
import ru.javarush.islandModel.model.animal.herbivore.*;
import ru.javarush.islandModel.model.animal.omnivore.Duck;
import ru.javarush.islandModel.model.animal.omnivore.Hog;
import ru.javarush.islandModel.model.animal.omnivore.Mouse;
import ru.javarush.islandModel.model.animal.predator.*;

import java.util.HashMap;
import java.util.Map;

public class DefaultSettings {
    public static final int WIDTH = 10;
    public  static final int LENGTH = 10;
    public  static final long DURING_OF_CYCLE = 10_000L;
    public  static final double PERCENT_WEIGHT_LOSS = 5.0;
    public  static final double PERCENT_REDUCED_SATIETY = 10.0;
    public  static final double MAX_WEIGHT_OF_PLANT = 200.0;
    public  static final String PLANT_PRINT = "\uD83C\uDF3F";
    public  static final Map<Class<? extends Animal>, Integer> MAX_COUNT_OF_STEPS = new HashMap<>();
    public  static final Map<Class<? extends Predator>, Map<Class<? extends Animal>, Integer>> FOOD_PREFERENCES = new HashMap<>();
    public  static final Map<Class<? extends Animal>, Double> DAILY_ALLOWANCE = new HashMap<>();
    public  static final Map<Class<? extends Animal>, Double> ANIMAL_AVG_WEIGHT = new HashMap<>();
    public  static final Map<Class<? extends Animal>, Integer> MAX_COUNT_BROOD = new HashMap<>();
    public  static final Map<Class<? extends Animal>, Integer> MAX_COUNT_ON_LOCATION = new HashMap<>();
    public  static final Map<Class<? extends Animal>, String> ANIMAL_PRINTS = new HashMap<>();
    public static final int CRITICAL_NUMBER_EXTINCT_TYPES = 1;
    public static boolean GET_ON_LOCATION_STATISTICS = false;
    public static final int CORE_POOL_SIZE = 4;

    static {
        MAX_COUNT_OF_STEPS.put(Wolf.class, 3);
        MAX_COUNT_OF_STEPS.put(Python.class, 1);
        MAX_COUNT_OF_STEPS.put(Fox.class, 2);
        MAX_COUNT_OF_STEPS.put(Bear.class, 2);
        MAX_COUNT_OF_STEPS.put(Eagle.class, 3);
        MAX_COUNT_OF_STEPS.put(Horse.class, 4);
        MAX_COUNT_OF_STEPS.put(Deer.class, 4);
        MAX_COUNT_OF_STEPS.put(Rabbit.class, 2);
        MAX_COUNT_OF_STEPS.put(Mouse.class, 1);
        MAX_COUNT_OF_STEPS.put(Goat.class, 3);
        MAX_COUNT_OF_STEPS.put(Sheep.class, 3);
        MAX_COUNT_OF_STEPS.put(Hog.class, 2);
        MAX_COUNT_OF_STEPS.put(Buffalo.class, 3);
        MAX_COUNT_OF_STEPS.put(Duck.class, 4);
        MAX_COUNT_OF_STEPS.put(Caterpillar.class, 0);

        FOOD_PREFERENCES.put(Wolf.class, Map.of(Horse.class, 10, Deer.class, 15, Rabbit.class, 60,
                Mouse.class, 80, Goat.class, 60, Sheep.class, 70, Hog.class, 15, Buffalo.class, 10,
                Duck.class, 40));
        FOOD_PREFERENCES.put(Python.class, Map.of(Fox.class, 15, Rabbit.class, 20, Mouse.class, 40,
                Duck.class, 10));
        FOOD_PREFERENCES.put(Fox.class, Map.of(Rabbit.class, 70, Mouse.class, 90, Duck.class, 60,
                Caterpillar.class, 40));
        FOOD_PREFERENCES.put(Bear.class, Map.of(Python.class, 80, Horse.class, 40, Deer.class, 80,
                Rabbit.class, 80, Mouse.class, 90, Goat.class, 70, Sheep.class, 70, Hog.class, 50,
                Buffalo.class, 20, Duck.class, 10));
        FOOD_PREFERENCES.put(Eagle.class, Map.of(Fox.class, 10, Rabbit.class, 90, Mouse.class, 90,
                Duck.class, 80));
        FOOD_PREFERENCES.put(Mouse.class, Map.of(Caterpillar.class, 90));
        FOOD_PREFERENCES.put(Hog.class, Map.of(Caterpillar.class, 90, Mouse.class, 50));
        FOOD_PREFERENCES.put(Duck.class, Map.of(Caterpillar.class, 90));

        DAILY_ALLOWANCE.put(Wolf.class, 8.0);
        DAILY_ALLOWANCE.put(Python.class, 3.0);
        DAILY_ALLOWANCE.put(Fox.class, 2.0);
        DAILY_ALLOWANCE.put(Bear.class, 80.0);
        DAILY_ALLOWANCE.put(Eagle.class, 1.0);
        DAILY_ALLOWANCE.put(Horse.class, 60.0);
        DAILY_ALLOWANCE.put(Deer.class, 50.0);
        DAILY_ALLOWANCE.put(Rabbit.class, 0.45);
        DAILY_ALLOWANCE.put(Mouse.class, 0.01);
        DAILY_ALLOWANCE.put(Goat.class, 10.0);
        DAILY_ALLOWANCE.put(Sheep.class, 15.0);
        DAILY_ALLOWANCE.put(Hog.class, 50.0);
        DAILY_ALLOWANCE.put(Buffalo.class, 100.0);
        DAILY_ALLOWANCE.put(Duck.class, 0.15);
        DAILY_ALLOWANCE.put(Caterpillar.class, 0.0);

        ANIMAL_AVG_WEIGHT.put(Wolf.class, 50.0);
        ANIMAL_AVG_WEIGHT.put(Python.class, 15.0);
        ANIMAL_AVG_WEIGHT.put(Fox.class, 8.0);
        ANIMAL_AVG_WEIGHT.put(Eagle.class, 6.0);
        ANIMAL_AVG_WEIGHT.put(Bear.class, 500.0);
        ANIMAL_AVG_WEIGHT.put(Sheep.class, 70.0);
        ANIMAL_AVG_WEIGHT.put(Rabbit.class, 4.0);
        ANIMAL_AVG_WEIGHT.put(Mouse.class, 0.05);
        ANIMAL_AVG_WEIGHT.put(Horse.class, 400.0);
        ANIMAL_AVG_WEIGHT.put(Hog.class, 400.0);
        ANIMAL_AVG_WEIGHT.put(Goat.class, 60.0);
        ANIMAL_AVG_WEIGHT.put(Duck.class, 1.0);
        ANIMAL_AVG_WEIGHT.put(Deer.class, 300.0);
        ANIMAL_AVG_WEIGHT.put(Caterpillar.class, 0.01);
        ANIMAL_AVG_WEIGHT.put(Buffalo.class, 700.0);

        MAX_COUNT_BROOD.put(Wolf.class, 6);
        MAX_COUNT_BROOD.put(Python.class, 3);
        MAX_COUNT_BROOD.put(Fox.class, 6);
        MAX_COUNT_BROOD.put(Eagle.class, 8);
        MAX_COUNT_BROOD.put(Bear.class, 3);
        MAX_COUNT_BROOD.put(Sheep.class, 3);
        MAX_COUNT_BROOD.put(Rabbit.class, 6);
        MAX_COUNT_BROOD.put(Mouse.class, 50);
        MAX_COUNT_BROOD.put(Horse.class, 1);
        MAX_COUNT_BROOD.put(Hog.class, 3);
        MAX_COUNT_BROOD.put(Goat.class, 4);
        MAX_COUNT_BROOD.put(Duck.class, 20);
        MAX_COUNT_BROOD.put(Deer.class, 3);
        MAX_COUNT_BROOD.put(Caterpillar.class, 40);
        MAX_COUNT_BROOD.put(Buffalo.class, 3);

        MAX_COUNT_ON_LOCATION.put(Wolf.class, 30);
        MAX_COUNT_ON_LOCATION.put(Buffalo.class, 10);
        MAX_COUNT_ON_LOCATION.put(Python.class, 30);
        MAX_COUNT_ON_LOCATION.put(Fox.class, 30);
        MAX_COUNT_ON_LOCATION.put(Bear.class, 5);
        MAX_COUNT_ON_LOCATION.put(Eagle.class, 20);
        MAX_COUNT_ON_LOCATION.put(Horse.class, 20);
        MAX_COUNT_ON_LOCATION.put(Deer.class, 20);
        MAX_COUNT_ON_LOCATION.put(Rabbit.class, 150);
        MAX_COUNT_ON_LOCATION.put(Mouse.class, 500);
        MAX_COUNT_ON_LOCATION.put(Goat.class, 140);
        MAX_COUNT_ON_LOCATION.put(Sheep.class, 140);
        MAX_COUNT_ON_LOCATION.put(Hog.class, 50);
        MAX_COUNT_ON_LOCATION.put(Duck.class, 200);
        MAX_COUNT_ON_LOCATION.put(Caterpillar.class, 1000);

        ANIMAL_PRINTS.put(Buffalo.class, "\uD83D\uDC03");
        ANIMAL_PRINTS.put(Bear.class, "\uD83D\uDC3B");
        ANIMAL_PRINTS.put(Horse.class, "\uD83D\uDC0E");
        ANIMAL_PRINTS.put(Deer.class, "\uD83E\uDD8C");
        ANIMAL_PRINTS.put(Hog.class, "\uD83D\uDC17");
        ANIMAL_PRINTS.put(Sheep.class, "\uD83D\uDC11");
        ANIMAL_PRINTS.put(Goat.class, "\uD83D\uDC10");
        ANIMAL_PRINTS.put(Wolf.class, "\uD83D\uDC3A");
        ANIMAL_PRINTS.put(Python.class, "\uD83D\uDC0D");
        ANIMAL_PRINTS.put(Fox.class, "\uD83E\uDD8A");
        ANIMAL_PRINTS.put(Eagle.class, "\uD83E\uDD85");
        ANIMAL_PRINTS.put(Rabbit.class, "\uD83D\uDC07");
        ANIMAL_PRINTS.put(Duck.class, "\uD83E\uDD86");
        ANIMAL_PRINTS.put(Mouse.class, "\uD83D\uDC01");
        ANIMAL_PRINTS.put(Caterpillar.class, "\uD83D\uDC1B");
    }
}
