package com.example.employeedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeFileDto {
    @ApiModelProperty(value = "Employee's first name")
    private String firstName;
    @ApiModelProperty(value = "Employee's last name")
    private String lastName;
    @ApiModelProperty(value = "Employee's birth date")
    private String birthDate;
    @ApiModelProperty(value = "Employee's role")
    private String role;
    @ApiModelProperty(value = "Employee's development language")
    private String devLanguage;
    @ApiModelProperty(value = "Employee's project ids")
    private String projectIds;
}
