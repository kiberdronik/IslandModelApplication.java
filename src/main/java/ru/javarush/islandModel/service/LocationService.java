package ru.javarush.islandModel.service;

import org.springframework.stereotype.Service;
import ru.javarush.islandModel.model.island.Location;
import ru.javarush.islandModel.repository.LocationRepository;

import java.util.List;

@Service
public class LocationService {

    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<Location> getLocations() {
        return locationRepository.getLocations();
    }

    public void initializeIsland(int width, int length) {
        locationRepository.initializeIsland(width, length);
    }
}
