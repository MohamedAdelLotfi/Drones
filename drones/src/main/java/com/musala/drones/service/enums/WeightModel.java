package com.musala.drones.service.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Mohamed
 * Weight Model */
public enum WeightModel {

    LIGHT_WEIGHT("LW"),
    MIDDLE_WEIGHT("MW"),
    CRUISER_WEIGHT("CW"),
    HEAVY_WEIGHT("HW");

    @Getter
    private String value;

    WeightModel(String value) {
        this.value = value;
    }

    public static WeightModel getWeightModel(String value) {
        return Arrays.stream(WeightModel.values())
                .filter(p -> Objects.equals(p.value, value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("WeightModel: ValueNotFound"));
    }
}
