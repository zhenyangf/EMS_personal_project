package com.example.demo.business.mappers;

import com.example.demo.business.repository.model.EmployeeDAO;
import com.example.demo.model.Employee;
import com.example.demo.model.Job;


public class EmployeeMapper {

    public static EmployeeDAO toEmployeeDao(Employee employee) {
        return EmployeeDAO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .dob(employee.getDob())
                .jobId(employee.getJob().getId())
                .build();
    }


    public static Employee toSaveEmployee(EmployeeDAO employeeDAO, Job job) {
        return Employee.builder()
                .id(employeeDAO.getId())
                .name(employeeDAO.getName())
                .surname(employeeDAO.getSurname())
                .dob(employeeDAO.getDob())
                .job(job)
                .build();
    }

}
