package com.example.demo.web.controller;

import com.example.demo.business.repository.EmployeeRepository;
import com.example.demo.business.repository.JobRepository;
import com.example.demo.business.repository.model.JobDAO;
import com.example.demo.model.Employee;
import com.example.demo.model.Job;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.JobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTestIT {
    public static String URL = "http://localhost:8080/api/job";
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    EmployeeController employeeController;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    JobService jobService;
    @Autowired
    JobController jobController;
    @MockBean
    JobRepository jobRepository;
    @MockBean
    EmployeeRepository employeeRepository;

    private JobDAO jobDAO = createJob();
    private Job job = createJob2();
    private List<JobDAO> jobDAOList = createJobDaoList();
    private List<Job> jobs = createJobList();

    @Test
    void testFindAllJobsRepository() throws Exception {
        when(jobRepository.findAll()).thenReturn(jobs);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value("test"))
                .andExpect(status().isOk());
        verify(jobRepository, times(2)).findAll();
    }
    @Test
    void testFindJobById() throws Exception{
        when(jobRepository.findById(1L)).thenReturn(Optional.ofNullable(job));
        mockMvc.perform(MockMvcRequestBuilders.get(URL+"/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("title").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("salary").value("test"))
                .andExpect(status().isOk());
        verify(jobRepository,times(1)).findById(1L);
    }
    @Test
    void testSaveEmployee() throws Exception{

        when(jobRepository.save(job)).thenReturn(job);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL + "/add")
                        .content(asJsonString(jobDAO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(jobRepository, times(1)).save(job);
    }
    @Test
    void testDeleteEmployeeById() throws Exception{
        Job job = createJob2();
        when(jobRepository.findById(anyLong())).thenReturn(Optional.of(job));
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/delete/1")
                        .content(asJsonString(job))
                        .contentType(MediaType.APPLICATION_JSON).
                        accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNoContent());
        verify(jobRepository, times(1)).deleteById(anyLong());

    }

    private JobDAO createJob() {
        JobDAO job = new JobDAO();
        job.setId(1L);
        job.setTitle("test");
        job.setSalary("test");
        return job;
    }

    private Job createJob2() {
        Job job = new Job();
        job.setId(1L);
        job.setTitle("test");
        job.setSalary("test");
        return job;
    }
    private List<Job> createJobList(){
        List<Job> joblist = new ArrayList<>();
        Job job1 = createJob2();
        Job job2 = createJob2();
        joblist.add(job1);
        joblist.add(job2);
        return joblist;
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<JobDAO> createJobDaoList() {
        List<JobDAO> list = new ArrayList<>();
        JobDAO job1 = createJob();
        JobDAO job2 = createJob();
        list.add(job1);
        list.add(job2);
        return list;
    }
}
