package com.musala.drones.service.Impl;

import com.musala.drones.domain.Medication;
import com.musala.drones.exceptions.BadRequestAlertException;
import com.musala.drones.repository.MedicationRepository;
import com.musala.drones.service.MedicationService;
import com.musala.drones.service.dto.MedicationDTO;
import com.musala.drones.service.mapper.MedicationMapper;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class MedicationServiceImpl implements MedicationService {

    private final Logger log = LoggerFactory.getLogger(MedicationServiceImpl.class);

    private final MedicationRepository medicationRepository;

    private final MedicationMapper medicationMapper;

    public MedicationServiceImpl(MedicationRepository medicationRepository, MedicationMapper medicationMapper) {
        this.medicationRepository = medicationRepository;
        this.medicationMapper = medicationMapper;
    }

    @Override
    public MedicationDTO save(MedicationDTO medicationDTO) {
        log.debug("Request to save Medication : {}", medicationDTO);
        Medication medication = medicationMapper.toEntity(medicationDTO);
        //
        checkNameExpression(medicationDTO.getName());
        checkCodeExpression(medicationDTO.getCode());
        //
        medication = medicationRepository.save(medication);
        return medicationMapper.toDto(medication);
    }

    @Override
    public Optional<MedicationDTO> partialUpdate(MedicationDTO medicationDTO) {
        log.debug("Request to partially update Medication : {}", medicationDTO);

        return medicationRepository
                .findById(medicationDTO.getId())
                .map(existingMedication -> {
                    medicationMapper.partialUpdate(existingMedication, medicationDTO);

                    return existingMedication;
                })
                .map(medicationRepository::save)
                .map(medicationMapper::toDto);
    }

    @Override
    public List<MedicationDTO> findAll() {
        List<Medication> medications = medicationRepository.findAll();
        return medications.stream().map(medicationMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all medications");
        return medicationRepository.findAll(pageable).map(medicationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MedicationDTO> findOne(Long id) {
        log.debug("Request to get Medication : {}", id);
        return medicationRepository.findById(id).map(medicationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Medication : {}", id);
        medicationRepository.deleteById(id);
    }

    @Override
    public String uploadMedicationImage(MultipartFile image, Long id) throws IOException {
        MedicationDTO dto = findOne(id).get();
        if (dto.getId() > 0) {
            dto.setImage(IOUtils.toByteArray(image.getInputStream()));
        }
        MedicationDTO dt = save(dto);
        if (dt != null && dt.getImage() != null && dt.getImage().length > 0) {
            return "FileUploadedSuccess";
        }
        return "CannotUploadFile";
    }

    @Override
    public Float getSumOfMedicationWeightByDroneId(Long drone_Id) {
        return medicationRepository.getSumOfMedicationWeightByDroneId(drone_Id);
    }

    /**
     * check name expression
     * @param name name to check on it matches expression or not
     * */
    private void checkNameExpression(String name) {
        if (name != null) {
            if (!name.matches("^[a-zA-Z0-9_]*$")) {
                throw new BadRequestAlertException("NameDon'tMatchExpression(Letters & Number & _)", "Medication", "Error");
            }
        }
    }

    /**
     * check code expression
     * @param code code to check on it matches expression or not
     * */
    private void checkCodeExpression(String code) {
        if (code != null) {
            if (!code.matches("^[A-Z0-9_]*$")) {
                throw new BadRequestAlertException("CodeDon'tMatchExpression(Letters & Number & _)", "Medication", "Error");
            }
        }
    }
}
