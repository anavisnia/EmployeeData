package com.example.employeedata.dto;

import java.time.LocalDate;

import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectDto {
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

    @NotNull(message = "must not be blank")
    private LocalDate terminationDate;

    @Min(0)
    @NotNull(message = "must not be blank")
    private Integer devLanguage;
    
}
