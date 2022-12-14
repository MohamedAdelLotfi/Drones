package com.musala.drones.service;

import com.musala.drones.service.dto.MedicationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface MedicationService {

    /**
     * Save a medication.
     *
     * @param medicationDTO the entity to save.
     * @return the persisted entity.
     */
    MedicationDTO save(MedicationDTO medicationDTO);

    /**
     * Partially updates a medication.
     *
     * @param medicationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MedicationDTO> partialUpdate(MedicationDTO medicationDTO);

    /**
     * Get all the medication.
     *
     * @return the list of entities.
     */
    List<MedicationDTO> findAll();

    /**
     * Get all the medications.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MedicationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" medication.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MedicationDTO> findOne(Long id);

    /**
     * Delete the "id" medication.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Upload Medication Image
     *
     * @param image file to upload it to medication
     * @param id id of entity
     * @return message of success uploading*/
    String uploadMedicationImage(MultipartFile image, Long id) throws IOException;

    /**
     * Sum Weight Medication By Drone Id
     *
     * @param drone_Id id of drone
     *
     * @return value of sum*/
    Float getSumOfMedicationWeightByDroneId(Long drone_Id);
}
