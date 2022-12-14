package com.example.employeedata.dto;

import java.time.LocalDate;
import java.util.*;

import javax.validation.constraints.*;

public class CreateEmployeeDto {
    @Pattern(regexp="^[a-zA-Z\\p{L}]+$", message = "Name must only consist of letters.")
    @NotEmpty(message = "Name must not be blank.")
    @Size(min = 3, max = 40)
    private String firstName;

    @Pattern(regexp="^[a-zA-Z\\p{L}]+$", message = "Surname must only consist of letters.")
    @NotEmpty(message = "Surname must not be blank.")
    @Size(min = 3, max = 100)
    private String lastName;

    private LocalDate birthDate;

    private Integer role;

    private Integer devLanguage;
    
    private List<Long> projectIds = new ArrayList<>();

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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getDevLanguage() {
        return devLanguage;
    }

    public void setDevLanguage(Integer devLanguage) {
        this.devLanguage = devLanguage;
    }

    public List<Long> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Long> projects) {
        this.projectIds = projects;
    }
}
