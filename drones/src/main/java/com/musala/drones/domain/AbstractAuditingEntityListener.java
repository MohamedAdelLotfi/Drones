package com.musala.drones.domain;

import javax.persistence.PreUpdate;
import java.time.Instant;

public class AbstractAuditingEntityListener {

        @PreUpdate
        public void preUpdate(AbstractAuditingEntity entity) {
            //
            entity.setLastModified(Instant.now());
            entity.setCreated(Instant.now());
            entity.setCreated_by("musalauser"); //TODO
            entity.setLast_modified_by("musalauser"); //TODO
        }

}
