package com.example.employeedata.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
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

    @Query(
        value = "SELECT e.first_name, e.last_name, e.birth_date, e.role, e.dev_language, IFNULL(GROUP_CONCAT(ep.project_id SEPARATOR ', '), '') as projectIds FROM employeedata.employees e LEFT JOIN employeedata.employee_project ep on ep.employee_id = e.id GROUP BY e.id",
        nativeQuery = true
    )
    List<Object[]> findAllEmployeesInclProjects();

    Page<Employee> findAll(Pageable pageable);

    @Query(
        value = "SELECT *, (CASE WHEN first_name LIKE :likeQuery THEN 0 WHEN last_name LIKE :likeQuery THEN 2 ELSE 1 END) AS 'relevance' FROM employeedata.employees WHERE regexp_like(first_name, :regexQuery, 'i') OR regexp_like(last_name, :regexQuery, 'i') ORDER BY relevance ASC",
        nativeQuery = true
    )
    Page<Employee> findAllFiltered(@Param("likeQuery") String likeQuery, @Param("regexQuery") String regexQuery, Pageable pageable);
}
