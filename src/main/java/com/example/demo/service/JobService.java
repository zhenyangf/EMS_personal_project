package com.example.demo.service;

import com.example.demo.business.mappers.JobMapper;
import com.example.demo.business.repository.JobRepository;
import com.example.demo.business.repository.model.JobDAO;
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
public class JobService  {
    @Autowired
    JobRepository jobRepository;

    public Optional<JobDAO> getJobById(Long id) {
        return jobRepository.findById(id).map(JobMapper::toJobDao);
    }

    public List<JobDAO> getAllJobs() {
        List<Job> jobList = jobRepository.findAll();
        if (jobList.isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.NO_CONTENT);
        }
        return jobRepository.findAll().stream().map(JobMapper::toJobDao).collect(Collectors.toList());
    }


    public JobDAO saveJob(JobDAO jobDAO) {
        Job saveJob = JobMapper.toJob(jobDAO);
        jobRepository.save(saveJob);
        return JobMapper.toJobDao(saveJob);
    }

    public void deleteJobById(Long id) {
        jobRepository.deleteById(id);
    }

    public Job getJobByIdNoDAO(Long id) {
        return jobRepository.findById(id).orElse(null);
    }
}
