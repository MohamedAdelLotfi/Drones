package com.musala.drones.service.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Mohamed
 * Drone State */
public enum DroneState {

    IDLE("ID"),
    LOADING("LN"),
    LOADED("LD"),
    DELIVERING("DR"),
    RETURNING("RT");

    @Getter
    private String value;

    DroneState(String value) {
        this.value = value;
    }

    public static DroneState getDroneState(String value) {
        return Arrays.stream(DroneState.values())
                .filter(p -> Objects.equals(p.value, value))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("DroneState: ValueNotFound"));
    }
}
