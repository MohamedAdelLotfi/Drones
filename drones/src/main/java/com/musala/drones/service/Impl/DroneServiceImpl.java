package com.musala.drones.service.Impl;

import com.musala.drones.domain.Drone;
import com.musala.drones.exceptions.BadRequestAlertException;
import com.musala.drones.repository.DroneRepository;
import com.musala.drones.service.DroneService;
import com.musala.drones.service.MedicationService;
import com.musala.drones.service.dto.DroneDTO;
import com.musala.drones.service.dto.MedicationDTO;
import com.musala.drones.service.enums.DroneState;
import com.musala.drones.service.enums.WeightModel;
import com.musala.drones.service.mapper.DroneMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class DroneServiceImpl implements DroneService {

    private final Logger log = LoggerFactory.getLogger(DroneServiceImpl.class);

    private final DroneRepository droneRepository;

    private final DroneMapper droneMapper;

    @Autowired
    MedicationService medicationService;

    public DroneServiceImpl(DroneRepository droneRepository, DroneMapper droneMapper) {
        this.droneRepository = droneRepository;
        this.droneMapper = droneMapper;
    }

    @Override
    public DroneDTO save(DroneDTO droneDTO) {
        log.debug("Request to save Drone : {}", droneDTO);
        Drone drone = droneMapper.toEntity(droneDTO);
        boolean isUpdate = false;
        if (droneDTO.getId() != null) {
            isUpdate = true;
        }
        //
        checkWeightLimit(droneDTO.getWeight_limit());
        checkBatteryCapacity(droneDTO.getBattery_capacity());
        checkSerialNumber(droneDTO.getSerial_number());
        validateDroneState(droneDTO);
        drone = droneRepository.save(drone);
        DroneDTO saveDto = droneMapper.toDto(drone);
        // add drone items
        if (!isUpdate) {
            if (droneDTO.getItems() != null && droneDTO.getItems().size() > 0) {
                createDroneItems(saveDto, droneDTO.getItems());
            }
        }
        return droneMapper.toDto(drone);
    }

    @Override
    public Optional<DroneDTO> partialUpdate(DroneDTO droneDTO) {
        log.debug("Request to partially update Drone : {}", droneDTO);

        return droneRepository
                .findById(droneDTO.getId())
                .map(existingDrone -> {
                    droneMapper.partialUpdate(existingDrone, droneDTO);
                    return existingDrone;
                })
                .map(droneRepository::save)
                .map(droneMapper::toDto);
    }

    @Override
    public List<DroneDTO> findAll() {
        List<Drone> drone = droneRepository.findAll();
        return drone.stream().map(droneMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DroneDTO> findAll(Pageable pageable) {
        log.debug("Request to get all drones");
        return droneRepository.findAll(pageable).map(droneMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DroneDTO> findOne(Long id) {
        log.debug("Request to get Drone : {}", id);
        Optional<DroneDTO> droneDTO =  droneRepository.findById(id).map(droneMapper::toDto);
        //
        String weightModel = droneDTO.get().getWeight_model();
        if (!StringUtils.isEmpty(weightModel)) {
            String weight_model = WeightModel.getWeightModel(weightModel).name();
            if (weight_model.length() == 2 && weight_model != null) {
                droneDTO.get().setWeight_model(weight_model);
            }
        }
        //
        String droneState = droneDTO.get().getDrone_state();
        if (!StringUtils.isEmpty(droneState)) {
            String drone_state = DroneState.getDroneState(droneState).name();
            if (drone_state.length() == 2 && drone_state != null) {
                droneDTO.get().setDrone_state(drone_state);
            }
        }
        return droneDTO;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Drone : {}", id);
        droneRepository.deleteById(id);
    }

    /**
     * check weight limit
     * @param weightLimit weight limit to check on it
     * */
    private void checkWeightLimit(BigDecimal weightLimit) {
        if (weightLimit != null) {
            if (weightLimit.compareTo(new BigDecimal(500)) > 0) {
                throw new BadRequestAlertException("WeightLimitShouldBeLessThan500", "Drone", "Error");
            }
        }
    }

    /**
     * check battery capacity
     * @param batteryCapacity battery capacity to check on it
     * */
    private void checkBatteryCapacity(BigDecimal batteryCapacity) {
        if (batteryCapacity != null) {
            if (batteryCapacity.compareTo(new BigDecimal(100)) > 0 || batteryCapacity.compareTo(new BigDecimal(0)) < 0) {
                throw new BadRequestAlertException("BatteryCapacityIsPercentageShouldBeBetween0-100", "Drone", "Error");
            }
        }
    }

    /**
     * check serial number
     * @param serialNumber serial number to check on it
     * */
    private void checkSerialNumber(String serialNumber) {
        if (serialNumber != null) {
            if (serialNumber.length() > 100) {
                throw new BadRequestAlertException("SerialNumberShouldBe100CharacterAtMaximum", "Drone", "Error");
            }
        }
    }

    /**
     * validate Drone Status
     *
     * @param dto object from drone
     * */
    private void validateDroneState(DroneDTO dto) {
        if (dto != null && dto.getDrone_state() != null) {
            //
            if (DroneState.LOADING.getValue().equals(dto.getDrone_state()) &&
                    dto.getBattery_capacity().compareTo(new BigDecimal(15)) < 0) {
                throw new BadRequestAlertException("DroneCannotLoading.Because,BatteryCapacityLessThan15%", "Drone", "Error");
            }
            Float medicationsWeight = medicationService.getSumOfMedicationWeightByDroneId(dto.getId());
            if (DroneState.LOADED.getValue().equals(dto.getDrone_state())
                    && medicationsWeight != null
                    && medicationsWeight.compareTo(dto.getWeight_limit().floatValue()) > 0) {
                throw new BadRequestAlertException("DroneCannotCarryItemsOverItsLimit", "Drone", "Error");
            }
        }
    }

    /**
     * Create Drone Medication Items
     *
     * @param dto object from drone created
     * @param droneItemDTOS list of medication items
     */
    private void createDroneItems(DroneDTO dto, Set<MedicationDTO> droneItemDTOS) {
        for (MedicationDTO medicationDTO : droneItemDTOS) {
            MedicationDTO itemDTO = new MedicationDTO();
            itemDTO.setDrone_id(dto.getId());
            itemDTO.setCode(medicationDTO.getCode());
            itemDTO.setName(medicationDTO.getName());
            itemDTO.setWeight(medicationDTO.getWeight());
            itemDTO.setCreated_by("musalauser");
            itemDTO.setLast_modified_by("musalauser");
            itemDTO.setIs_active(true);
            medicationService.save(itemDTO);
        }
    }
}
