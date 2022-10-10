package com.example.demo.business.repository.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDAO {
    private Long id;
    private String name;
    private String surname;
    private String dob;
    private Long job_id;

}
