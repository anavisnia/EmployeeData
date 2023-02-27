package com.example.employeedata.entity;

import java.time.*;
import java.util.*;

import javax.persistence.*;

import com.example.employeedata.enums.*;
import com.fasterxml.jackson.annotation.*;

import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ZonedDateTime getTerminationDate() {
        return terminationDate;
    }

    public void setTerminationDate(ZonedDateTime terminationDate) {
        this.terminationDate = terminationDate;
    }

    public ZonedDateTime getCompletionDate() {
        return completionDate;
    }

    public void setCompletionDate(ZonedDateTime completionDate) {
        this.completionDate = completionDate;
    }

    public DevLanguage getDevLanguage() {
        return devLanguage;
    }

    public void setDevLanguage(DevLanguage devLanguage) {
        this.devLanguage = devLanguage;
    }

    public ZonedDateTime getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(ZonedDateTime modificationDate) {
        this.modificationDate = modificationDate;
    }
}
