package com.example.employeedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
    @ApiModelProperty(value = "Entity's id")
    private Long id;
    @ApiModelProperty(value = "Custom response message")
    private String status;

    public ResponseDto(Long id) {
        this.id = id;
    }

    public ResponseDto(Long id, String instanceName) {
        this.id = id;
        this.status = instanceName + " created successfully";
    }

    public ResponseDto(Long id, String instanceName, String message) {
        this.id = id;
        this.status = instanceName + " " + message;
    }
    
}
