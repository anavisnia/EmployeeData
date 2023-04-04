package com.example.employeedata.repository;

import java.time.LocalDate;
import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

import com.example.employeedata.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Query(
        value = "SELECT * \n" +
                "FROM employeedata.employees E \n" +
                "INNER JOIN employeedata.employee_project EP ON E.id = EP.employee_id \n" +
                "WHERE EP.project_id = :projectId",
        nativeQuery = true
    )
    List<Employee> findByProjectId(@Param("projectId") Long projectId);

    @Query(
        value = "SELECT * \n" +
                "FROM employeedata.employees E \n" +
                "WHERE E.dev_language = :devLanguage",
        nativeQuery = true
    )
    List<Employee> findByDevLanguage(@Param("devLanguage") Integer devLanguage);

    @Query(
        value = "SELECT * \n" +
                "FROM employeedata.employees E \n" +
                "WHERE E.role = :role",
        nativeQuery = true
    )
    List<Employee> findByRole(@Param("role") Integer role);

    @Query(
        value = "SELECT e.first_name,\n" +
                    "e.last_name, \n" +
                    "e.birth_date, \n" +
                    "e.role, \n" +
                    "e.dev_language, \n"+
                    "IFNULL(GROUP_CONCAT(ep.project_id SEPARATOR ', '), '') AS projectIds\n" +
                "FROM employeedata.employees e \n" +
                "LEFT JOIN employeedata.employee_project ep ON ep.employee_id = e.id \n" +
                "GROUP BY e.id",
        nativeQuery = true
    )
    List<Object[]> findAllEmployeesInclProjects();

    Page<Employee> findAll(Pageable pageable);

    @Query(
        value = "SELECT *, \n" +
                "(CASE WHEN first_name LIKE :likeQuery THEN 0 \n" +
                "WHEN last_name LIKE :likeQuery THEN 2 ELSE 1 END) AS 'relevance' \n" +
                "FROM employeedata.employees \n" +
                "WHERE regexp_like(first_name, :regexQuery, 'i') OR \n" +
                    "regexp_like(last_name, :regexQuery, 'i') \n" +
                "ORDER BY relevance ASC",
        nativeQuery = true
    )
    Page<Employee> findAllByQuery(@Param("likeQuery") String likeQuery, @Param("regexQuery") String regexQuery, Pageable pageable);

    @Query(
            value = "SELECT * \n" +
                    "FROM employeedata.employees E \n" +
                    "WHERE E.first_name = :fName AND E.last_name = :lName AND E.birth_date = :birthDate",
            nativeQuery = true
    )
    Optional<Employee> findByFullNameAndBirthDate(@Param("fName") String fName, @Param("lName") String lName, @Param("birthDate") LocalDate birthDate);
}
