package com.musala.drones.service.dto;

import com.musala.drones.domain.AbstractAuditingEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class MedicationDTO extends AbstractAuditingEntity implements Serializable {

    private Long id;

    private String name;

    private BigDecimal weight;

    private String code;

    private byte[] image;

    private Long drone_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getDrone_id() {
        return drone_id;
    }

    public void setDrone_id(Long drone_id) {
        this.drone_id = drone_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedicationDTO)) {
            return false;
        }

        MedicationDTO medicationDTO = (MedicationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medicationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public String toString() {
        return "MedicationDTO{" +
                "id=" + getId() +
                ", name=" + getName() +
                ", weight='" + getWeight() + "'" +
                ", code='" + getCode() + "'" +
                ", is_active='" + getIs_active() + "'" +
                "}";
    }
}
