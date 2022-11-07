package com.musala.drones.service.mapper;

import com.musala.drones.domain.Medication;
import com.musala.drones.service.dto.MedicationDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface MedicationMapper extends EntityMapper<MedicationDTO, Medication> {

    MedicationDTO toDto(Medication medication);
}
