package com.example.employeedata.dto;

import java.time.LocalDate;
import java.util.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String role;
    private String devLanguage;
    private List<Long> projectIds = new ArrayList<>();

}
