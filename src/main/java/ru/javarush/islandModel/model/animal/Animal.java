package ru.javarush.islandModel.model.animal;

import lombok.*;
import ru.javarush.islandModel.model.island.Coordinate;
import ru.javarush.islandModel.settings.Settings;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class Animal {
    protected double currentWeight;
    protected boolean isMale;
    protected double saturationWithFood;
    protected Coordinate currentCoordinate;
    private static final double PERCENT_WEIGHT_LOSS = Settings.getSettings().getPercentWeightLoss();
    private static final double PERCENT_REDUCED_SATIETY = Settings.getSettings().getPercentReducedSatiety();

    public void starve() {
        currentWeight -= currentWeight * PERCENT_WEIGHT_LOSS / 100;
        saturationWithFood -= saturationWithFood * PERCENT_REDUCED_SATIETY / 100;
    }

    public abstract boolean isExhausted();

    public abstract boolean isHungry();
}
