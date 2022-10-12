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
        Optional <EmployeeDAO> employee= employeeRepository.findById(id).map(EmployeeMapper::toEmployeeDao);
        return employee;
    }


    @Override
    public List<EmployeeDAO> getAllEmployees() {
        List<Employee> employeeDAOS = employeeRepository.findAll();
        if(employeeDAOS.isEmpty()){
            throw new HttpClientErrorException(HttpStatus.NO_CONTENT);
        }
        List<EmployeeDAO> employeeDAOList = employeeRepository.findAll().stream().map(EmployeeMapper::toEmployeeDao).collect(Collectors.toList());
        return employeeDAOList;
    }

    @Override
    public Employee saveEmployee(EmployeeDAO newEmployee) {
        Job job = jobService.getJobByIdNoDAO(newEmployee.getJob_id());
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
        Employee employee = employeeRepository.findById(id).orElse(null);
        return employee;
    }
}
