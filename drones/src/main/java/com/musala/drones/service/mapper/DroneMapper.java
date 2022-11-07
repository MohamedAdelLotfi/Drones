package com.musala.drones.service.mapper;

import com.musala.drones.domain.Drone;
import com.musala.drones.service.dto.DroneDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {})
public interface DroneMapper extends EntityMapper<DroneDTO, Drone> {

    DroneDTO toDto(Drone drone);
}
