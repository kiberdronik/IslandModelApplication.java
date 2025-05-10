package ru.javarush.islandModel.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnimalTypeInfo {
    private String print;
    private int totalCount;
    private int male;
    private int female;
    private double averageSaturationWithFood;

    @Override
    public String toString() {
        return String.format("%s %-6s Самцы %-6s Самки %-6s   средняя насыщенность пищей %.1f%%", print,
                totalCount, male, female, averageSaturationWithFood);
    }
}
