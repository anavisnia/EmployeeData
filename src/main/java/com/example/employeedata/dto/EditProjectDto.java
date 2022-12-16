package com.example.employeedata.dto;

import java.time.LocalDate;

import javax.validation.constraints.*;

public class EditProjectDto {
    @Pattern(regexp="^[a-zA-Z0-9 \\p{L}]+$", message = "must only consist of letters and numbers")
    @NotEmpty(message = "must not be blank")
    @Size(min = 3, max = 500)
    private String title;

    @Max(100)
    private Integer teamSize;

    @Pattern(regexp = "^[A-Za-z0-9- \\p{L},._-|]+$", message = "should only consist of letters, numbers or symbols `,`, `.`, `-`, `_`, `|`")
    @NotEmpty(message = "must not be blank")
    @Size(min = 3, max = 500)
    private String customer;

    private LocalDate terminationDate;

    @Min(0)
    private Integer devLanguage;

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
    
}
