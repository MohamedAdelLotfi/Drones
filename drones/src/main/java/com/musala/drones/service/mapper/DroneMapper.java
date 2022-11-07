package com.musala.drones.service.mapper;

import com.musala.drones.domain.Drone;
import com.musala.drones.service.dto.DroneDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface DroneMapper extends EntityMapper<DroneDTO, Drone> {

    DroneDTO toDto(Drone drone);
}
