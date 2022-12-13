package com.example.employeedata.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

import com.example.employeedata.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    @Query(
        value = "SELECT * FROM employeedata.employees E INNER JOIN employeedata.employee_project EP ON E.id = EP.employee_id WHERE EP.project_id = :id",
        nativeQuery = true
    )
    List<Employee> findAllEmployeesByProjectId(@Param("id")Long id);
}
