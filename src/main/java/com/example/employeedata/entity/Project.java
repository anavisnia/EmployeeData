package com.example.employeedata.entity;

import java.time.*;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.*;

import com.example.employeedata.entity.helpers.DevLanguage;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Title must not be blank")
    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    @Max(100)
    private int teamSize;

    @NotEmpty(message = "customer name must not be blank")
    @Column(nullable = false)
    private String customer;

    @NotEmpty(message = "Termination date must not be blank")
    @Column(nullable = false)
    private LocalDate terminationDate;

    @Column(nullable = false)
    private DevLanguage devLanguage;

    @ManyToMany(mappedBy = "projects")
    @JsonBackReference
    private Set<Employee> employees = new HashSet<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(int teamSize) {
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
