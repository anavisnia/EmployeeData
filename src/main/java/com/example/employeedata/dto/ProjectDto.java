package com.example.employeedata.dto;

import java.time.LocalDate;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    @ApiModelProperty(value = "Project's title")
    private String title;
    @ApiModelProperty(value = "Project's team size")
    private Integer teamSize;
    @ApiModelProperty(value = "Project's cutomer name")
    private String customer;
    @ApiModelProperty(value = "Project's termination date")
    private LocalDate terminationDate;
    @ApiModelProperty(value = "Project's development language")
    private String devLanguage;

}
