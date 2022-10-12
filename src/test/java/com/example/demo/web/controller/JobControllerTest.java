package com.example.demo.web.controller;

import com.example.demo.business.repository.model.EmployeeDAO;
import com.example.demo.business.repository.model.JobDAO;
import com.example.demo.business.service.EmployeeService;
import com.example.demo.business.service.JobService;
import com.example.demo.model.Job;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JobControllerTest {
    public static String URL = "http://localhost:8080/api/job";
    @MockBean
    JobService jobService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeController employeeController;
    @Autowired
    private JobController jobController;
    @MockBean
    private EmployeeService employeeService;

    private JobDAO jobDAO = createJob();
    private List<JobDAO> jobDAOList = createJobList();

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindAllJobs() throws Exception {
        when(jobService.getAllJobs()).thenReturn(jobDAOList);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].salary").value("test"))
                .andExpect(status().isOk());
        verify(jobService, times(1)).getAllJobs();

    }

    @Test
    void testFindAllJobsInvalid() throws Exception {
        List<JobDAO> jobDAOList = createJobList();
        jobDAOList.clear();
        when(jobService.getAllJobs()).thenReturn(jobDAOList);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/all"))
                .andExpect(status().isNotFound());
        verify(jobService, times(1)).getAllJobs();
    }

    @Test
    void testFindJobById() throws Exception {
        Optional<JobDAO> jobDAO = Optional.of(createJob());
        when(jobService.getJobById(anyLong())).thenReturn(jobDAO);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.salary").value("test"))
                .andExpect(status().isOk());
        verify(jobService, times(1)).getJobById(anyLong());

    }

    @Test
    void testFindJobByInvalidId() throws Exception {
        when(jobService.getJobById(anyLong())).thenReturn(Optional.empty());
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/4"))
                .andExpect(status().isNotFound());
        verify(jobService, times(0)).getJobById(null);
    }

    @Test
    void testSaveJob() throws Exception {
        JobDAO jobDAO = createJob();


        when(jobService.saveJob(jobDAO)).thenReturn(jobDAO);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL + "/add")
                        .content(asJsonString(jobDAO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(jobService, times(1)).saveJob(jobDAO);
    }
    @Test
    void testDeleteJob() throws Exception {
        Optional<JobDAO> jobDAO = Optional.of(createJob());
        when(jobService.getJobById(anyLong())).thenReturn(jobDAO);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/delete/1")
                .content(asJsonString(jobDAO))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
        verify(jobService, times(1)).deleteJobById(anyLong());
    }
    @Test
    void testDeleteInvalidEmployee() throws Exception {
        Optional<JobDAO> jobDAO = Optional.of(createJob());
        jobDAO.get().setId(null);
        when(jobService.getJobById(null)).thenReturn(jobDAO);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/delete/1")
                        .content(asJsonString(jobDAO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(jobService, times(0)).deleteJobById(null);
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

    private List<JobDAO> createJobList() {
        List<JobDAO> list = new ArrayList<>();
        JobDAO job1 = createJob();
        JobDAO job2 = createJob();
        list.add(job1);
        list.add(job2);
        return list;
    }
}
