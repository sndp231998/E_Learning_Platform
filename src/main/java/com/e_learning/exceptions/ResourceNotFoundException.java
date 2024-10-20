package com.e_learning.exceptions;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

    String resourceName;
    String fieldName;
    String fieldValue;
    private List<String> userFacult;

    public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s : %d", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = Long.toString(fieldValue);
    }

    
    
    public ResourceNotFoundException(String resourceName, String fieldName, List<String> userFacult) {
        super(String.format("%s not found with %s : %d", resourceName, fieldName, userFacult));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.userFacult = userFacult;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String fieldValue) {
        super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
