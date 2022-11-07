package com.musala.drones.domain;

import com.musala.drones.exceptions.BadRequestAlertException;
import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "drone")
@Setter
@Getter
public class Drone extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "serial_number", columnDefinition = "nvarchar2(100)")
    private String serial_number;

    @NotNull
    @Column(name = "weight_model", columnDefinition = "char(2)")
    private String weight_model;

    @NotNull
    @Column(name = "weight_limit", columnDefinition = "float")
    private BigDecimal weight_limit;

    @NotNull
    @Column(name = "battery_capacity", columnDefinition = "float")
    private BigDecimal battery_capacity;

    @NotNull
    @Column(name = "drone_state", columnDefinition = "char(2)")
    private String drone_state;

    @OneToMany(mappedBy = "drone_id")
    private Set<Medication> items;

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

    public Set<Medication> getItems() {
        return items;
    }

    public void setItems(Set<Medication> items) {
        this.items = items;
    }

    /**
     * To Check id of object equals to current object or instance of it
     *
     * return boolean value
     * Example:
     * It is reflexive: for any non-null reference value x, x.equals(x) should return true.
     * It is symmetric: for any non-null reference values x and y, x.equals(y) should return true if and only if y.equals(x) returns true.
     * It is transitive: for any non-null reference values x, y, and z, if x.equals(y) returns true and y.equals(z) returns true, then x.equals(z) should return true.
     * It is consistent: for any non-null reference values x and y, multiple invocations of x.equals(y) consistently return true or consistently return false, provided no information used in equals comparisons on the objects is modified.
     * For any non-null reference value x, x.equals(null) should return false.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Drone)) {
            return false;
        }
        return id != null && id.equals(((Drone) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        if (getId() == null || getId() <= 0) {
            throw new BadRequestAlertException("idNotExists", "Drone", "notFound");
        }
        return "Drone{" +
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
