package com.example.employeedata.entity;

import java.time.*;
import java.util.*;

import javax.persistence.*;

import com.example.employeedata.enums.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private Integer teamSize;

    @Column(nullable = false)
    private String customer;

    @Column(nullable = false)
    private LocalDate terminationDate;

    @Column(nullable = false)
    private DevLanguage devLanguage;

    @ManyToMany(mappedBy = "projects")
    @JsonBackReference
    private Set<Employee> employees = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public LocalDate getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(LocalDate terminationDate) {
        this.terminationDate = terminationDate;
    }

    public DevLanguage getDevLanguage() {
        return devLanguage;
    }

    public void setDevLanguage(DevLanguage devLanguage) {
        this.devLanguage = devLanguage;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

}
