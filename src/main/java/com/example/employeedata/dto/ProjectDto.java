package com.example.employeedata.dto;

import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDto {
    @ApiModelProperty(value = "Project's title")
    private String title;
    @ApiModelProperty(value = "Project's description")
    private String description;
    @ApiModelProperty(value = "Project's team size")
    private Integer teamSize;
    @ApiModelProperty(value = "Project's customer name")
    private String customer;
    @ApiModelProperty(value = "Project's termination date")
    private LocalDateTime terminationDate;
    @ApiModelProperty(value = "Project's completion date")
    private LocalDateTime completionDate;
    @ApiModelProperty(value = "Project's development language")
    private String devLanguage;

}
