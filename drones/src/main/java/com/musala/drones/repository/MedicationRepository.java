package com.musala.drones.repository;

import com.musala.drones.domain.Medication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicationRepository extends JpaRepository<Medication, Long>, JpaSpecificationExecutor<Medication> {

    @Query(value = "select sum(mr.weight) from medication mr where mr.drone_id =:drone_id", nativeQuery = true)
    Float getSumOfMedicationWeightByDroneId(@Param("drone_id") Long drone_Id);
}
