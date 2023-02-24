package com.example.employeedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultQueryDto {
    @ApiModelProperty(value = "Took time to execute query")
    private Float tookTime;
    @ApiModelProperty(value = "Number of result found by a provided query")
    private Integer numberOfResults;
    @ApiModelProperty(value = "String representation of found entities")
    private String elements;
}
