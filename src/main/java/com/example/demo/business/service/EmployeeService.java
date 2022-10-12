package com.example.demo.business.service;


import com.example.demo.business.repository.model.EmployeeDAO;
import com.example.demo.model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface EmployeeService {

    Optional<EmployeeDAO> getEmployeeById(Long id);

    List<EmployeeDAO> getAllEmployees();

    Employee saveEmployee(EmployeeDAO newEmployee);

    void deleteEmployeeById(Long id);

    Employee getEmployeeByIdNoDAO(Long id);
}
