package com.example.employeedata.entity;

import java.time.*;
import java.util.*;

import javax.persistence.*;

import com.example.employeedata.entity.helpers.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public DevLanguage getDevLanguage() {
        return devLanguage;
    }

    public void setDevLanguage(DevLanguage devLanguage) {
        this.devLanguage = devLanguage;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

}
