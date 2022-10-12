package com.example.demo.business.service;

import com.example.demo.business.repository.model.JobDAO;
import com.example.demo.model.Job;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface JobService {
    Optional<JobDAO> getJobById(Long id);

    List<JobDAO> getAllJobs();

    JobDAO saveJob(JobDAO jobDAO);

    void deleteJobById(Long id);

    Job getJobByIdNoDAO(Long id);


}
