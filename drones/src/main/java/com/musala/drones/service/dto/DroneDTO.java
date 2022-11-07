package com.musala.drones.service.dto;

import com.musala.drones.domain.AbstractAuditingEntity;
import com.musala.drones.domain.Medication;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

public class DroneDTO extends AbstractAuditingEntity implements Serializable {

    private Long id;

    private String serial_number;

    private String weight_model;

    private BigDecimal weight_limit;

    private BigDecimal battery_capacity;

    private String drone_state;

    private Set<MedicationDTO> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getWeight_model() {
        return weight_model;
    }

    public void setWeight_model(String weight_model) {
        this.weight_model = weight_model;
    }

    public BigDecimal getWeight_limit() {
        return weight_limit;
    }

    public void setWeight_limit(BigDecimal weight_limit) {
        this.weight_limit = weight_limit;
    }

    public BigDecimal getBattery_capacity() {
        return battery_capacity;
    }

    public void setBattery_capacity(BigDecimal battery_capacity) {
        this.battery_capacity = battery_capacity;
    }

    public String getDrone_state() {
        return drone_state;
    }

    public void setDrone_state(String drone_state) {
        this.drone_state = drone_state;
    }

    public Set<MedicationDTO> getItems() {
        return items;
    }

    public void setItems(Set<MedicationDTO> items) {
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DroneDTO)) {
            return false;
        }

        DroneDTO droneDTO = (DroneDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, droneDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "DroneDTO{" +
                "id=" + getId() +
                ", serial_number=" + getSerial_number() +
                ", weight_model='" + getWeight_model() + "'" +
                ", weight_limit='" + getWeight_limit() + "'" +
                ", battery_capacity='" + getBattery_capacity() + "'" +
                ", drone_state='" + getDrone_state() + "'" +
                ", is_active='" + getIs_active() + "'" +
                "}";
    }
}
