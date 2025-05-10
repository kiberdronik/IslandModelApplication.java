package ru.javarush.islandModel.model.animal;

import ru.javarush.islandModel.model.animal.herbivore.*;
import ru.javarush.islandModel.model.animal.omnivore.Duck;
import ru.javarush.islandModel.model.animal.omnivore.Hog;
import ru.javarush.islandModel.model.animal.omnivore.Mouse;
import ru.javarush.islandModel.model.animal.predator.*;
import ru.javarush.islandModel.model.island.Coordinate;

import java.util.*;
import java.util.stream.Collectors;

public class AnimalFactory {

    public static List<Animal> getCertainNumberOfAnimals(Class<? extends Animal> clazz, int count, Coordinate coordinate) {
        return Arrays.stream(new int[count])
                .mapToObj(iteration -> createAnimal(clazz)).filter(Objects::nonNull)
                .peek(animal -> animal.setCurrentCoordinate(coordinate))
                .collect(Collectors.toList());
    }

    private static Animal createAnimal(Class<? extends Animal> clazz) {
        return switch (clazz.getSimpleName()) {
            case "Wolf" -> new Wolf();
            case "Python" -> new Python();
            case "Fox" -> new Fox();
            case "Eagle" -> new Eagle();
            case "Bear" -> new Bear();
            case "Sheep" -> new Sheep();
            case "Rabbit" -> new Rabbit();
            case "Mouse" -> new Mouse();
            case "Horse" -> new Horse();
            case "Hog" -> new Hog();
            case "Goat" -> new Goat();
            case "Duck" -> new Duck();
            case "Deer" -> new Deer();
            case "Caterpillar" -> new Caterpillar();
            case "Buffalo" -> new Buffalo();
            default -> null;
        };
    }
}
