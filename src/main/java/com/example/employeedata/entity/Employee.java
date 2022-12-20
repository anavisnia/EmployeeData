package com.example.employeedata.entity;

import java.time.*;
import java.util.*;

import javax.persistence.*;

import com.example.employeedata.enums.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private DevLanguage devLanguage;

    @ManyToMany
    @JoinTable(
        name = "employee_project",
        joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id")
    )
    @JsonManagedReference
    private Set<Project> projects = new HashSet<>();

}
