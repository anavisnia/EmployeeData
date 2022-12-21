package com.example.employeedata.repository;

import java.time.LocalDate;
import java.util.*;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import com.example.employeedata.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query(
        value = "SELECT * FROM employeedata.projects P WHERE P.termination_date > :terminationDate",
        nativeQuery = true
    )
    List<Project> findByFutureTerminationDate(@Param("terminationDate") LocalDate terminationDate);

    @Query(
        value = "SELECT * FROM employeedata.projects P WHERE P.termination_date < :terminationDate",
        nativeQuery = true
    )
    List<Project> findByPriorTerminationDate(@Param("terminationDate") LocalDate terminationDate);

    @Query(
        value = "SELECT * FROM employeedata.projects P WHERE P.dev_language = :devLanguage",
        nativeQuery = true
    )
    List<Project> findByDevLanguage(@Param("devLanguage") Integer devLanguage);
}
