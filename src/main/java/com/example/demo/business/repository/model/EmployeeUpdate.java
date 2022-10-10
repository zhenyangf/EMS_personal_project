package com.example.demo.business.repository.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeUpdate {
    private String name;
    private String surname;
    private String dob;

}
