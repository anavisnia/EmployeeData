package com.example.employeedata.dto;

import java.time.LocalDate;

import javax.validation.constraints.*;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditProjectDto {
    @Pattern(regexp="^[a-zA-Z0-9 \\p{L}]+$", message = "must only consist of letters and numbers")
    @NotEmpty(message = "must not be blank")
    @Size(min = 3, max = 500)
    @ApiModelProperty(value = "Project's title")
    private String title;

    @Pattern(regexp="^[a-zA-Z0-9 \\p{L}]+$", message = "must only consist of letters and numbers")
    @Size(min = 0, max = 1000)
    @ApiModelProperty(value = "Project's description")
    private String description;

    @Max(100)
    @ApiModelProperty(value = "Project's team size")
    private Integer teamSize;

    @Pattern(regexp = "^[A-Za-z0-9- \\p{L},._-|]+$", message = "should only consist of letters, numbers or symbols `,`, `.`, `-`, `_`, `|`")
    @NotEmpty(message = "must not be blank")
    @Size(min = 3, max = 500)
    @ApiModelProperty(value = "Project's customer name")
    private String customer;

    @NotNull(message = "must not be blank")
    @ApiModelProperty(value = "Project's termination date")
    private LocalDate terminationDate;

    @Min(0)
    @NotNull(message = "must not be blank")
    @ApiModelProperty(value = "Project's development language id")
    private Integer devLanguage;

}
