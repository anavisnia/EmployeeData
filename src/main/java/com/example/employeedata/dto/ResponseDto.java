package com.example.employeedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
    @ApiModelProperty(value = "Entity's id")
    private Object id;
    @ApiModelProperty(value = "Custom response message")
    private String status;

    public ResponseDto(Object id) {
        this.id = id;
    }

    public ResponseDto(Object id, String instanceName) {
        this.id = id;
        this.status = instanceName + " created successfully";
    }

    public ResponseDto(Object id, String instanceName, String message) {
        this.id = id;
        this.status = instanceName + " " + message;
    }
    
}
