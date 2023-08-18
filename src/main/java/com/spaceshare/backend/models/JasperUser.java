package com.spaceshare.backend.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JasperUser {
    private String userType;
    private int userValue;
}
