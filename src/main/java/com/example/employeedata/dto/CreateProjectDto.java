package com.example.employeedata.dto;

import java.time.LocalDate;
import java.util.*;

import javax.validation.constraints.*;

public class CreateProjectDto {
    @Pattern(regexp="^[a-zA-Z0-9 \\p{L}]+$", message = "Name must only consist of letters and numbers.")
    @NotEmpty(message = "Title must not be blank.")
    @Size(min = 3, max = 500)
    private String title;

    @Max(100)
    private Integer teamSize;

    @Pattern(regexp = "^[A-Za-z0-9- \\p{L},._-|]+$", message = "should only consist of letters, numbers or symbols `,`, `.`, `-`, `_`, `|`.")
    @NotEmpty(message = "Customer must not be blank.")
    @Size(min = 3, max = 500)
    private String customer;

    private LocalDate terminationDate;

    private Integer devLanguage;
    
    private List<Long> employeeIds = new ArrayList<>();

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

    public Integer getDevLanguage() {
        return devLanguage;
    }

    public void setDevLanguage(Integer devLanguage) {
        this.devLanguage = devLanguage;
    }

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }
    
}
