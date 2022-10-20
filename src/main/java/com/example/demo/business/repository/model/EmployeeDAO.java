package com.example.demo.business.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDAO {
    private Long id;
    @NotBlank(message = "Name shouldn't be blank")
    @NotNull(message = "name shouldnt be null")
    private String name;
    @NotBlank(message = "Surname shouldn't be blank")
    private String surname;
    private String dob;
    @NotNull(message = "Job Id required")
    private Long jobId;

}
