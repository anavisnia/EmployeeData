package com.example.employeedata.dto;

import java.time.LocalDate;
import java.util.*;

import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDto {
    @Pattern(regexp="^[a-zA-Z\\p{L}]+$", message = "must only consist of letters")
    @NotEmpty(message = "must not be blank")
    @Size(min = 3, max = 40)
    private String firstName;

    @Pattern(regexp="^[a-zA-Z\\p{L}]+$", message = "must only consist of letters")
    @NotEmpty(message = "must not be blank")
    @Size(min = 3, max = 100)
    private String lastName;

    @NotNull(message = "must not be blank")
    private LocalDate birthDate;

    @Min(0)
    @NotNull(message = "must not be blank")
    private Integer role;

    @Min(0)
    @NotNull(message = "must not be blank")
    private Integer devLanguage;
    
    private List<Long> projectIds = new ArrayList<>();

}
