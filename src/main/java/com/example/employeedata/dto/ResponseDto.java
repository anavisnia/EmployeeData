package com.example.employeedata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ResponseDto {
    @ApiModelProperty(value = "Success response entity")
    private SuccessResponse successResponse = new SuccessResponse();
    @ApiModelProperty(value = "Failed response entity")
    private FailedResponse failedResponse = new FailedResponse();

    @Getter
    @Setter
    @NoArgsConstructor
    static class SuccessResponse {
        @ApiModelProperty(value = "Entity")
        private Object sucessObject;
        @ApiModelProperty(value = "Custom response message")
        private String sucessStatus = "Success processing the object.";
    }

    @Getter
    @Setter
    @NoArgsConstructor
    static class FailedResponse {
        @ApiModelProperty(value = "Entity")
        private Object failObject;
        @ApiModelProperty(value = "Custom response message")
        private String status = "Failed to process the object.";
    }

    public ResponseDto() {}

    public ResponseDto(Object object, boolean isFailedResponse) {
        if (!isFailedResponse) {
            this.successResponse.sucessObject = object;
            this.failedResponse = null;
        } else {
            this.failedResponse.failObject = object;
            this.successResponse = null;
        }
    }

    public ResponseDto(Object object, String instanceName, boolean isFailedResponse) {
        if (!isFailedResponse) {
            this.successResponse.sucessObject = object;
            this.successResponse.sucessStatus = instanceName + " created successfully.";
            this.failedResponse = null;
        } else {
            this.failedResponse.failObject = object;
            this.failedResponse.status += " Instance: " + instanceName;
            this.successResponse = null;
        }
    }

    public ResponseDto(Object object, String instanceName, String message, boolean isFailedResponse) {
        if (!isFailedResponse) {
            this.successResponse.sucessObject = object;
            this.successResponse.sucessStatus = instanceName + " " + message + ".";
            this.failedResponse = null;
        } else {
            this.failedResponse.failObject = object;
            this.failedResponse.status = instanceName + " " + message + ".";
            this.successResponse = null;
        }
    }

    public ResponseDto(Object sucessObject, String sucessMessage, Object failedObject) {
        this.successResponse.sucessObject = sucessObject;
        this.successResponse.sucessStatus = sucessMessage + ".";
        this.failedResponse.failObject = failedObject;
    }

    public ResponseDto(Object sucessObject, String sucessMessage, Object failedObject, String failMessage) {
        this.successResponse.sucessObject = sucessObject;
        this.successResponse.sucessStatus = sucessMessage + ".";
        this.failedResponse.failObject = failedObject;
        this.failedResponse.status = failMessage + ".";
    }
    
}
