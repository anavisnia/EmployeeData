package com.example.employeedata.dto;

import java.time.ZonedDateTime;

import javax.validation.constraints.*;

import com.example.employeedata.helpers.Constants;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectDto {
    @Pattern(regexp = Constants.REGEX_TEXT_WITHOUT_SYMBOLS, message = "must only consist of letters and numbers")
    @NotEmpty(message = "must not be blank")
    @Size(min = 3, max = 500)
    @ApiModelProperty(value = "Project's title")
    private String title;

    @Pattern(regexp = Constants.REGEX_TEXT_WITH_SYMBOLS, message = "must only consist of letters and numbers")
    @Size(min = 0, max = 1000)
    @ApiModelProperty(value = "Project's description")
    private String description;

    @Max(100)
    @ApiModelProperty(value = "Project's team size")
    private Integer teamSize;

    @Pattern(regexp = Constants.REGEX_TEXT_WITH_SYMBOLS, message = "should only consist of letters, numbers or symbols `,`, `.`, `-`, `_`, `|`")
    @NotEmpty(message = "must not be blank")
    @Size(min = 3, max = 500)
    @ApiModelProperty(value = "Project's customer name")
    private String customer;

    @NotNull(message = "must not be blank")
    @ApiModelProperty(value = "Project's termination date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime terminationDate;

    @ApiModelProperty(value = "Project's completion date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private ZonedDateTime completionDate;

    @Min(0)
    @NotNull(message = "must not be blank")
    @ApiModelProperty(value = "Project's development language id")
    private Integer devLanguage;
    
}
