package com.musala.drones.controller;

import com.musala.drones.controller.response.EntityId;
import com.musala.drones.controller.response.HeaderUtil;
import com.musala.drones.controller.response.ResponseUtil;
import com.musala.drones.exceptions.BadRequestAlertException;
import com.musala.drones.repository.MedicationRepository;
import com.musala.drones.service.MedicationService;
import com.musala.drones.service.dto.MedicationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/v1/api")
public class MedicationController {

    private final Logger log = LoggerFactory.getLogger(MedicationController.class);

    private static final String ENTITY_NAME = "medication";
    private static final String applicationName = "MusalaDrones";

    private final MedicationService medicationService;

    private final MedicationRepository medicationRepository;

    public MedicationController(MedicationService medicationService, MedicationRepository medicationRepository) {
        this.medicationService = medicationService;
        this.medicationRepository = medicationRepository;
    }

    @PostMapping(
            value = "/medication",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public EntityId<Long> createMedication(@RequestBody MedicationDTO medicationDTO)
            throws URISyntaxException {
        log.debug("REST request to save Medication : {}", medicationDTO);
        if (medicationDTO.getId() != null) {
            throw new BadRequestAlertException("A new Medication cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MedicationDTO result = medicationService.save(medicationDTO);

        EntityId<Long> entity = new EntityId<>();
        entity.setId(result.getId());
        return entity;
    }

    @PutMapping(
            value = "/medication/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<MedicationDTO> updateMedication(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody MedicationDTO medicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Medication : {}, {}", id, medicationDTO);
        if (medicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MedicationDTO result = medicationService.save(medicationDTO);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @PatchMapping(
            value = "/medication/{id}",
            consumes = { "application/json", "application/merge-patch+json" },
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<MedicationDTO> partialUpdateMedication(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody MedicationDTO medicationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Medication : {}, {}", id, medicationDTO);
        if (medicationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, medicationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!medicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MedicationDTO result = medicationService.save(medicationDTO);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @GetMapping(
            value = "/medication/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<MedicationDTO> getMedication(@PathVariable Long id) {
        log.debug("REST request to get Medication : {}", id);
        Optional<MedicationDTO> medicationDTO = medicationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medicationDTO);
    }

    @GetMapping(
            value = "/medication-pageable",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<Page<MedicationDTO>> getMedicationPageable(Pageable pageable) {
        log.debug("REST request to get Medication : {}", pageable);
        Page<MedicationDTO> medicationDTOS = medicationService.findAll(pageable);
        return ResponseEntity.ok(medicationDTOS);
    }

    @GetMapping(
            value = "/medications",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<List<MedicationDTO>> getMedicationAll() {
        log.debug("REST request to get Medication : {}");
        List<MedicationDTO> medicationDTOS = medicationService.findAll();
        return ResponseEntity.ok(medicationDTOS);
    }

    @DeleteMapping("/medication/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        log.debug("REST request to delete medication : {}", id);
        medicationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
