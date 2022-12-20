package com.example.employeedata.dto;

import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    private String title;
    private Integer teamSize;
    private String customer;
    private LocalDate terminationDate;
    private String devLanguage;

}
