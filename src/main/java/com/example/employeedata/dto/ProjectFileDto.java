package com.example.employeedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectFileDto {
    @ApiModelProperty(value = "Project's title")
    private String title;
    @ApiModelProperty(value = "Project's description")
    private String description;
    @ApiModelProperty(value = "Project's cutomer name")
    private String customer;
    @ApiModelProperty(value = "Project's team size")
    private String teamSize;
    @ApiModelProperty(value = "Project's development language")
    private String devLanguage;
    @ApiModelProperty(value = "Project's termination date")
    private String terminationDate;
}
