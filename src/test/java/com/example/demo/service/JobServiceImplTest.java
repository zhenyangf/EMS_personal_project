package com.example.demo.service;

import com.example.demo.business.repository.JobRepository;
import com.example.demo.business.repository.model.JobDAO;
import com.example.demo.model.Employee;
import com.example.demo.model.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class JobServiceImplTest {
    @Mock
    private JobRepository jobRepository;
    @InjectMocks
    private JobService jobService;

    private Job job1;
    private Job job2;
    private Employee employee1;
    private Employee employee2;
    private List<Employee> employees;
    private List<Job> jobs;
    private JobDAO jobDAO;

    @BeforeEach
    public void init() {
        job1 = createJob(1L, "test", "test");
        jobs = createJobList(job1);
        employee1 = createEmployee(1L, "test2", "test2", "test2", job1);
        employees = createEmployeeList(employee1);
        jobDAO = createJobDao(1L, "test", "test");
        job2 = createJob(1L, "test", "test");
        employee2 = createEmployee(1L, "test2", "test2", "test2", job2);
    }

    @Test
    void testFindAllJobs() throws Exception {
        when(jobRepository.findAll()).thenReturn(jobs);
        List<JobDAO> jobs = jobService.getAllJobs();
        assertEquals(2, jobs.size());
        verify(jobRepository, times(2)).findAll();
    }

    @Test
    void testFindAllJobsInvalid() throws Exception {
        when(jobRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(HttpClientErrorException.class, () -> jobService.getAllJobs());
    }

    @Test
    void testFindJobById() throws Exception {
        given(jobRepository.findById(anyLong())).willReturn(Optional.of(job1));

        final Optional<JobDAO> expected = jobService.getJobById(anyLong());
        assertTrue(expected.isPresent());
        assertEquals(expected, Optional.of(jobDAO));
        verify(jobRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindJobByIdNoDAO() throws Exception {
        given(jobRepository.findById(anyLong())).willReturn(Optional.ofNullable(job1));

        Job expected = jobService.getJobByIdNoDAO(anyLong());
        assertEquals(expected, job1);
        verify(jobRepository, times(1)).findById(anyLong());
    }

    @Test
    void testSaveJob() throws Exception {
        when(jobRepository.save(job1)).thenReturn(job1);
        JobDAO jobsaved = jobService.saveJob(jobDAO);
        assertEquals(job1.getTitle(), jobsaved.getTitle());
        assertEquals(job1.getSalary(), jobsaved.getSalary());
        assertEquals(job1.getId(), jobsaved.getId());
        verify(jobRepository, times(1)).save(job1);
    }

    @Test
    void testDeleteJob() throws Exception {
        jobService.deleteJobById(anyLong());
        verify(jobRepository, times(1)).deleteById(anyLong());
    }


    private Job createJob(Long id, String title, String salary) {
        job1 = new Job();
        job1.setId(id);

        job1.setTitle(title);
        job1.setSalary(salary);
        return job1;
    }

    private JobDAO createJobDao(Long id, String title, String salary) {
        jobDAO = new JobDAO();
        jobDAO.setId(id);
        jobDAO.setTitle(title);
        jobDAO.setSalary(salary);
        return jobDAO;
    }

    private List<Job> createJobList(Job job) {
        List<Job> list = new ArrayList<>();
        list.add(job);
        list.add(job);
        return list;
    }

    private List<Employee> createEmployeeList(Employee employee) {
        List<Employee> list = new ArrayList<>();
        list.add(employee);
        return list;
    }

    private Employee createEmployee(Long id, String name, String surname, String dob, Job job) {
        employee1 = new Employee();
        employee1.setId(id);
        employee1.setName(name);
        employee1.setSurname(surname);
        employee1.setDob(dob);
        employee1.setJob(job);
        return employee1;
    }

}
