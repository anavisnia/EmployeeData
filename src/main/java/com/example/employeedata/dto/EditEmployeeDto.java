package com.example.employeedata.dto;

import java.util.*;

import javax.validation.constraints.*;

public class EditEmployeeDto {
    @Pattern(regexp="^[a-zA-Z\\p{L}]+$", message = "Name must only consist of letters.")
    @NotEmpty(message = "Name must not be blank.")
    @Size(min = 3, max = 40)
    private String firstName;

    @Pattern(regexp="^[a-zA-Z\\p{L}]+$", message = "Surname must only consist of letters.")
    @NotEmpty(message = "Surname must not be blank.")
    @Size(min = 3, max = 100)
    private String lastName;
    
    private int role;

    private int devLanguage;
    
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

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getDevLanguage() {
        return devLanguage;
    }

    public void setDevLanguage(int devLanguage) {
        this.devLanguage = devLanguage;
    }

    public List<Long> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Long> projectIds) {
        this.projectIds = projectIds;
    }
    
}
