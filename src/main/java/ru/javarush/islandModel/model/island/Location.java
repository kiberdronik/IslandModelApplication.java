package ru.javarush.islandModel.model.island;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.javarush.islandModel.model.Plant;
import ru.javarush.islandModel.model.animal.*;
import ru.javarush.islandModel.settings.Settings;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Location {
    private Coordinate coordinate;
    private Map<Class<? extends Animal>, List<Animal>> animals = new ConcurrentHashMap<>();
    private boolean isRiver;
    private Plant plant;
    private final Lock lock = new ReentrantLock(true);

    public Location(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public List<Animal> getAllAnimals() {
        return animals.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public boolean isItPossibleToAddAnimal(Class<? extends Animal> clazz) {
        return !isRiver && Settings.getSettings().getMaxCountOnLocation().get(clazz) > animals.get(clazz).size();
    }

    public List<Animal> getEatablesOfCertainTypes(Set<Class<? extends Animal>> eatableTypes) {
        return eatableTypes.stream()
                .map(clazz -> animals.get(clazz))
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public boolean isReproductionPossible(Animal animal) {
        return animals.get(animal.getClass()).stream()
                .anyMatch(localAnimal -> localAnimal.isMale() != animal.isMale());
    }

    public void addAnimal(Animal animal) {
        animals.get(animal.getClass()).add(animal);
    }

    public void addBrood(Class<? extends Animal> clazz, List<Animal> brood) {
        animals.get(clazz).addAll(brood);
    }

    public void removeAnimal(Animal animal) {
        animals.get(animal.getClass()).remove(animal);
    }

    public int getCountOfCertainType(Class<? extends Animal> clazz) {
        return animals.get(clazz).size();
    }

    @Override
    public String toString() {
        if (isRiver) return "coordinate=" + coordinate + ": " + "Место для синей реки...";
        StringBuilder builder = new StringBuilder();
        animals.forEach((clazz, animalsOfCertainType) ->
                builder.append(Settings.getSettings().getAnimalPrints().get(clazz))
                        .append(" ")
                        .append(animalsOfCertainType.size())
                        .append(" "));
        return "coordinate=" + coordinate + ": " + builder;
    }
}
