package com.example.demo.business.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDAO {
    private Long id;
    @NotEmpty
    @NonNull
    private String name;
    @NotEmpty
    @NonNull
    private String surname;
    private String dob;
    @NotEmpty
    @NonNull
    private Long jobId;

}
