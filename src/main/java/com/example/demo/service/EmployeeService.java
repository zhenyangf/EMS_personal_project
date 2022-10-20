package com.example.demo.service;

import com.example.demo.business.mappers.EmployeeMapper;
import com.example.demo.business.repository.EmployeeRepository;
import com.example.demo.business.repository.model.EmployeeDAO;
import com.example.demo.model.Employee;
import com.example.demo.model.Job;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService  {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    JobService jobService;



    public Optional<EmployeeDAO> getEmployeeById(Long id) {

        return  employeeRepository.findById(id).map(EmployeeMapper::toEmployeeDao);
    }



    public List<EmployeeDAO> getAllEmployees() {
        List<Employee> employeeDAOS = employeeRepository.findAll();
        return employeeRepository.findAll().stream().map(EmployeeMapper::toEmployeeDao).collect(Collectors.toList());
    }


    public Employee saveEmployee(EmployeeDAO newEmployee) {
        Job job = jobService.getJobByIdNoDAO(newEmployee.getJobId());
        Employee saveEmployee = EmployeeMapper.toSaveEmployee(newEmployee, job);
        employeeRepository.save(saveEmployee);
        return EmployeeMapper.toSaveEmployee(newEmployee, job);
    }



    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }


    public Employee getEmployeeByIdNoDAO(Long id) {
        return employeeRepository.findById(id).orElse(null);

    }
}
