package com.example.demo.business.service.impl;

import com.example.demo.business.mappers.EmployeeMapper;
import com.example.demo.business.repository.EmployeeRepository;
import com.example.demo.business.repository.model.EmployeeDAO;
import com.example.demo.business.service.EmployeeService;
import com.example.demo.business.service.JobService;
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
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    JobService jobService;


    @Override
    public Optional<EmployeeDAO> getEmployeeById(Long id) {
        return  employeeRepository.findById(id).map(EmployeeMapper::toEmployeeDao);
    }


    @Override
    public List<EmployeeDAO> getAllEmployees() {
        List<Employee> employeeDAOS = employeeRepository.findAll();
        if (employeeDAOS.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NO_CONTENT);
        }
        return employeeRepository.findAll().stream().map(EmployeeMapper::toEmployeeDao).collect(Collectors.toList());
    }

    @Override
    public Employee saveEmployee(EmployeeDAO newEmployee) {
        Job job = jobService.getJobByIdNoDAO(newEmployee.getJobId());
        Employee saveEmployee = EmployeeMapper.toSaveEmployee(newEmployee, job);
        if (saveEmployee.getJob() == null) {

            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        employeeRepository.save(saveEmployee);
        return EmployeeMapper.toSaveEmployee(newEmployee, job);
    }


    @Override
    public void deleteEmployeeById(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee getEmployeeByIdNoDAO(Long id) {
        return employeeRepository.findById(id).orElse(null);

    }
}
