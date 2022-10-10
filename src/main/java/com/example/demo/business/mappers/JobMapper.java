package com.example.demo.business.mappers;

import com.example.demo.business.repository.model.EmployeeDAO;
import com.example.demo.business.repository.model.JobDAO;
import com.example.demo.model.Employee;
import com.example.demo.model.Job;

public class JobMapper {


    public static JobDAO toJobDao(Job job){
        return JobDAO.builder()
                .id(job.getId())
                .title(job.getTitle())
                .salary(job.getSalary())
                .build();
    }

    public static Job toJob(JobDAO jobDAO){
        return Job.builder()
                .id(jobDAO.getId())
                .title(jobDAO.getTitle())
                .salary(jobDAO.getSalary())
                .build();
    }
}
