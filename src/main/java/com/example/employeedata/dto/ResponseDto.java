package com.example.employeedata.dto;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
