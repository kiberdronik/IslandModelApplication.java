package ru.javarush.islandModel.model.island;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Coordinate {
    private int x;
    private int y;
}
