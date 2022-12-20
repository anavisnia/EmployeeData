package com.example.employeedata.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
    private Long id;
    private String status;

    public ResponseDto(Long id) {
        this.id = id;
    }

    public ResponseDto(Long id, String instanceName) {
        this.id = id;
        this.status = instanceName + " created successfully";
    }
    
}
