package ru.javarush.islandModel.model.animal.herbivore;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import ru.javarush.islandModel.model.animal.Animal;
import ru.javarush.islandModel.settings.Settings;

import java.util.concurrent.ThreadLocalRandom;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
public class Caterpillar extends Animal implements Herbivore {
    private static double weightAvg = Settings.getSettings().getWeightAvg().get(Caterpillar.class);
    private static double dailyAllowance = Settings.getSettings().getDailyAllowance().get(Caterpillar.class);

    public Caterpillar() {
        currentWeight = weightAvg;
        isMale = ThreadLocalRandom.current().nextBoolean();
        saturationWithFood = dailyAllowance;
    }

    @Override
    public boolean isHungry() {
        return true;
    }

    @Override
    public boolean isExhausted() {
        return currentWeight <= weightAvg / 2;
    }
}
