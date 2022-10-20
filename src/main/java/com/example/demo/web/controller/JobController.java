package com.example.demo.web.controller;

import com.example.demo.business.repository.JobRepository;
import com.example.demo.business.repository.model.JobDAO;
import com.example.demo.handlers.ExceptionHandler;
import com.example.demo.service.JobService;
import com.sun.istack.NotNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Log4j2
@RestController
@RequestMapping("/api/job")
public class JobController {
    @Autowired
    JobRepository jobRepository;
    @Autowired
    JobService jobService;

    @GetMapping("/all")
    public ResponseEntity<List<JobDAO>> findAllJobs() {
        List<JobDAO> jobDAOList = jobService.getAllJobs();
        if (jobDAOList.isEmpty()) {
            throw new ExceptionHandler("list is empty");}
        return ResponseEntity.ok(jobDAOList);}

    @GetMapping("/{id}")
    public ResponseEntity<JobDAO> findJobById(@NotNull @PathVariable Long id) {
        Optional<JobDAO> job = jobService.getJobById(id);
        if (!job.isPresent() || id < 0) {
            throw new ExceptionHandler("Job with id not found - " + id);}
        return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<JobDAO> saveJob(@Valid @RequestBody JobDAO jobDAO) {
        JobDAO jobSaved = jobService.saveJob(jobDAO);
        return new ResponseEntity<>(jobSaved, HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteJobId(@PathVariable Long id) {
        Optional<JobDAO> jobDAO = jobService.getJobById(id);
        if (!(jobDAO.isPresent()) || id < 0) {
            throw new ExceptionHandler("No job with id found" + id);}
        jobService.deleteJobById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
