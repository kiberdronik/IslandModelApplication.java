package ru.javarush.islandModel.thread.tasks;

import ru.javarush.islandModel.model.animal.Animal;
import ru.javarush.islandModel.service.AnimalService;

public class AnimalLocalTask extends LocalTask {
    private final Animal animal;
    private final AnimalService animalService;

    public AnimalLocalTask(Animal animal, AnimalService animalService) {
        this.animal = animal;
        this.animalService = animalService;
    }

    public void execute() {
        if (animal.isHungry()) animalService.eat(animal);
        if (!animal.isHungry()) animalService.reproduce(animal);
        animalService.move(animal);
    }
}
