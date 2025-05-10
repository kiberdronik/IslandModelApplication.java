package ru.javarush.islandModel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.javarush.islandModel.settings.Settings;
import java.util.concurrent.ThreadLocalRandom;

@Data
@AllArgsConstructor
@Builder
public class Plant {
    private static final double MAX_WEIGHT = Settings.getSettings().getMaxWeightOfPlant();
    private double currentWeight;

    public Plant() {
        this.currentWeight = ThreadLocalRandom.current().nextDouble(MAX_WEIGHT);
    }

    public void grow() {
        double delta = MAX_WEIGHT - currentWeight;
        currentWeight += ThreadLocalRandom.current().nextDouble(delta);
    }
}
