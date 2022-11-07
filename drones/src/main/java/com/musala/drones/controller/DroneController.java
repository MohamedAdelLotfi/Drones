package com.musala.drones.controller;

import com.musala.drones.controller.response.EntityId;
import com.musala.drones.controller.response.HeaderUtil;
import com.musala.drones.controller.response.ResponseUtil;
import com.musala.drones.exceptions.BadRequestAlertException;
import com.musala.drones.repository.DroneRepository;
import com.musala.drones.service.DroneService;
import com.musala.drones.service.dto.DroneDTO;
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
public class DroneController {

    private final Logger log = LoggerFactory.getLogger(DroneController.class);

    private static final String ENTITY_NAME = "drone";
    private static final String applicationName = "MusalaDrones";

    private final DroneService droneService;

    private final DroneRepository droneRepository;

    public DroneController(DroneService droneService, DroneRepository droneRepository) {
        this.droneService = droneService;
        this.droneRepository = droneRepository;
    }

    @PostMapping(
            value = "/drone",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    @ResponseStatus(value = HttpStatus.CREATED)
    public EntityId<Long> createDrone(@RequestBody DroneDTO droneDTO)
            throws URISyntaxException {
        log.debug("REST request to save Drone : {}", droneDTO);
        if (droneDTO.getId() != null) {
            throw new BadRequestAlertException("A new Drone cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DroneDTO result = droneService.save(droneDTO);

        EntityId<Long> entity = new EntityId<>();
        entity.setId(result.getId());
        return entity;
    }

    @PutMapping(
            value = "/drone/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<DroneDTO> updateDrone(
            @PathVariable(value = "id", required = false) final Long id,
            @RequestBody DroneDTO droneDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Drone : {}, {}", id, droneDTO);
        if (droneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, droneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!droneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DroneDTO result = droneService.save(droneDTO);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @PatchMapping(
            value = "/drone/{id}",
            consumes = { "application/json", "application/merge-patch+json" },
            produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<DroneDTO> partialUpdateDrone(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody DroneDTO droneDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Drone : {}, {}", id, droneDTO);
        if (droneDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, droneDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!droneRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DroneDTO result = droneService.save(droneDTO);
        return ResponseEntity
                .ok()
                .body(result);
    }

    @GetMapping(
            value = "/drone/{id}",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<DroneDTO> getDrone(@PathVariable Long id) {
        log.debug("REST request to get Drone : {}", id);
        Optional<DroneDTO> droneDTO = droneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(droneDTO);
    }

    @GetMapping(
            value = "/drone-pageable",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<Page<DroneDTO>> getDronePageable(Pageable pageable) {
        log.debug("REST request to get Drone : {}", pageable);
        Page<DroneDTO> droneDTOs = droneService.findAll(pageable);
        return ResponseEntity.ok(droneDTOs);
    }

    @GetMapping(
            value = "/drones",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE }
    )
    public ResponseEntity<List<DroneDTO>> getDroneAll() {
        log.debug("REST request to get Drone : {}");
        List<DroneDTO> droneDTOS = droneService.findAll();
        return ResponseEntity.ok(droneDTOS);
    }

    @DeleteMapping("/drone/{id}")
    public ResponseEntity<Void> deleteDrone(@PathVariable Long id) {
        log.debug("REST request to delete drone : {}", id);
        droneService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
