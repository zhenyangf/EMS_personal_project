package com.example.demo.business.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class JobDAO {
    private Long id;
    private String title;
    private String salary;
}
