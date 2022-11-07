package com.musala.drones.service.mapper;

import com.musala.drones.domain.Medication;
import com.musala.drones.service.dto.MedicationDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {})
public interface MedicationMapper extends EntityMapper<MedicationDTO, Medication> {

    MedicationDTO toDto(Medication medication);
}
