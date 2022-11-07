package com.musala.drones.service.mapper;

import com.musala.drones.domain.Medication;
import com.musala.drones.service.dto.MedicationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {})
public interface MedicationMapper extends EntityMapper<MedicationDTO, Medication> {


    @Mapping(target = "id", source = "id")
    MedicationDTO toDto(Medication medication);
}
