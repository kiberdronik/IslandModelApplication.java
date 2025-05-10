package ru.javarush.islandModel.thread;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import ru.javarush.islandModel.service.AnimalService;
import ru.javarush.islandModel.service.LocationService;
import ru.javarush.islandModel.settings.Settings;

@Component
public class Runner implements CommandLineRunner {
    private final AnimalService animalService;
    private final LocationService locationService;

    public Runner(AnimalService animalService, LocationService locationService) {
        this.animalService = animalService;
        this.locationService = locationService;
    }

    @Override
    public void run(String... args) throws Exception {
        locationService.initializeIsland(Settings.getSettings().getWidth(), Settings.getSettings().getLength());
        IslandManager islandManager = new IslandManager(locationService, animalService);
        islandManager.start();
    }
}
