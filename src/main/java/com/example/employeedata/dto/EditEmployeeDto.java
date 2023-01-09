package com.example.employeedata.dto;

import java.util.*;

import javax.validation.constraints.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditEmployeeDto {
    @Pattern(regexp="^[a-zA-Z\\p{L}]+$", message = "must only consist of letters")
    @NotEmpty(message = "must not be blank")
    @Size(min = 3, max = 40)
    @ApiModelProperty(value = "Employee's first name")
    private String firstName;

    @Pattern(regexp="^[a-zA-Z\\p{L}]+$", message = "must only consist of letters")
    @NotEmpty(message = "must not be blank")
    @Size(min = 3, max = 100)
    @ApiModelProperty(value = "Employee's last name")
    private String lastName;
    
    @Min(0)
    @NotNull(message = "must not be blank")
    @ApiModelProperty(value = "Employee's role in the company")
    private Integer role;

    @Min(0)
    @NotNull(message = "must not be blank")
    @ApiModelProperty(value = "Employee's main development language")
    private Integer devLanguage;
    
    @ApiModelProperty(value = "Employee's project ids")
    private List<Long> projectIds = new ArrayList<>();
    
}
