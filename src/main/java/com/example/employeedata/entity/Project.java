package com.example.employeedata.entity;

import java.time.*;
import java.util.*;

import javax.persistence.*;

import com.example.employeedata.enums.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "teamSize")
    private Integer teamSize;

    @Column(nullable = false, name = "customer")
    private String customer;

    @Column(nullable = false, name = "terminationDate")
    private ZonedDateTime terminationDate;

    @Column(name = "completionDate")
    private ZonedDateTime completionDate;

    @Column(nullable = false, name = "devLanguage")
    private DevLanguage devLanguage;

    @Column(nullable = false)
    private ZonedDateTime modificationDate;

    @ManyToMany(mappedBy = "projects")
    @JsonBackReference
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Set<Employee> employees = new HashSet<>();

}
