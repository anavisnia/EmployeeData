package com.example.employeedata.dto;

import java.time.LocalDate;
import java.util.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {
    @ApiModelProperty(value = "Employee's first name")
    private String firstName;
    @ApiModelProperty(value = "Employee's last name")
    private String lastName;
    @ApiModelProperty(value = "Employee's birth date")
    private LocalDate birthDate;
    @ApiModelProperty(value = "Employee's role")
    private String role;
    @ApiModelProperty(value = "Employee's development language")
    private String devLanguage;
    @ApiModelProperty(value = "Employee's project ids")
    private List<Long> projectIds = new ArrayList<>();
}
