package ru.javarush.islandModel.repository;

import ru.javarush.islandModel.model.animal.Animal;
import ru.javarush.islandModel.model.island.Coordinate;
import ru.javarush.islandModel.model.island.Location;

import java.util.List;

public interface LocationRepository {

    void initializeIsland(int width, int length);
    List<Coordinate> getNearestAvailableCoordinates(Animal animal);
    List<Location> getLocations();
    Location getLocationByCoordinate(Coordinate coordinate);
}
