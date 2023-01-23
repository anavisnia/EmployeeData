package com.example.employeedata.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

import com.example.employeedata.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(
        value = "SELECT * FROM employeedata.employees E INNER JOIN employeedata.employee_project EP ON E.id = EP.employee_id WHERE EP.project_id = :projectId",
        nativeQuery = true
    )
    List<Employee> findByProjectId(@Param("projectId") Long projectId);

    @Query(
        value = "SELECT * FROM employeedata.employees E WHERE E.dev_language = :devLanguage",
        nativeQuery = true
    )
    List<Employee> findByDevLanguage(@Param("devLanguage") Integer devLanguage);

    @Query(
        value = "SELECT * FROM employeedata.employees E WHERE E.role = :role",
        nativeQuery = true
    )
    List<Employee> findByRole(@Param("role") Integer role);
}
