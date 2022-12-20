package com.example.employeedata.dto;

import java.util.*;

import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditEmployeeDto {
    @Pattern(regexp="^[a-zA-Z\\p{L}]+$", message = "must only consist of letters")
    @NotEmpty(message = "must not be blank")
    @Size(min = 3, max = 40)
    private String firstName;

    @Pattern(regexp="^[a-zA-Z\\p{L}]+$", message = "must only consist of letters")
    @NotEmpty(message = "must not be blank")
    @Size(min = 3, max = 100)
    private String lastName;
    
    @Min(0)
    private Integer role;

    @Min(0)
    private Integer devLanguage;
    
    private List<Long> projectIds = new ArrayList<>();
    
}
