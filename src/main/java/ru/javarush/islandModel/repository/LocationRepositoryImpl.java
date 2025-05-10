package ru.javarush.islandModel.repository;

import org.springframework.stereotype.Repository;
import ru.javarush.islandModel.utils.RandomService;
import ru.javarush.islandModel.model.Plant;
import ru.javarush.islandModel.model.animal.Animal;
import ru.javarush.islandModel.model.island.Coordinate;
import ru.javarush.islandModel.model.island.Location;
import ru.javarush.islandModel.enums.Direction;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class LocationRepositoryImpl implements LocationRepository {
    private final Map<Coordinate, Location> locations = new ConcurrentHashMap<>();

    @Override
    public void initializeIsland(int width, int length) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                Coordinate currentCoordinate = new Coordinate(i, j);
                locations.put(currentCoordinate, new Location(currentCoordinate));
            }
        }
        if (isThereRiverOnIsland(width, length)) setRiverCoordinates(width, length);
        populateWithAnimalsAndPlants();
    }

    @Override
    public List<Location> getLocations() {
        return new ArrayList<>(locations.values());
    }

    @Override
    public Location getLocationByCoordinate(Coordinate coordinate) {
        return locations.get(coordinate);
    }

    @Override
    public List<Coordinate> getNearestAvailableCoordinates(Animal animal) {
        int x = animal.getCurrentCoordinate().getX();
        int y = animal.getCurrentCoordinate().getY();

        return Stream.of(Direction.NORTH, Direction.NORTH_EAST, Direction.NORTH_WEST, Direction.EAST, Direction.SOUTH,
                        Direction.SOUTH_EAST, Direction.SOUTH_WEST, Direction.WEST)
                .map(direction -> new Coordinate(x + direction.getDeltaX(), y + direction.getDeltaY()))
                .filter(locations::containsKey)
                .filter(coordinate -> !locations.get(coordinate).isRiver())
                .filter(coordinate -> locations.get(coordinate).isItPossibleToAddAnimal(animal.getClass()))
                .collect(Collectors.toList());
    }

    private boolean isThereRiverOnIsland(int width, int length) {
        return width > 1 && length > 2 || width > 2 && length > 1;
    }

    private void setRiverCoordinates(int width, int length) {
        for (int i = 0; i < Math.min(length, width); i++) {
            locations.get(new Coordinate(i, Math.max(width, length)/2)).setRiver(true);
        }
    }

    private void populateWithAnimalsAndPlants() {
        locations.values().stream()
                .filter(location -> !location.isRiver())
                .peek(location -> location.setPlant(new Plant()))
                .forEach(location -> location.setAnimals(RandomService.getAnimalsRandomCountOnLocation(location.getCoordinate())));
    }
}
