package com.example.demo.web.controller;

import com.example.demo.business.mappers.EmployeeMapper;
import com.example.demo.business.repository.EmployeeRepository;
import com.example.demo.business.repository.JobRepository;
import com.example.demo.business.repository.model.EmployeeDAO;
import com.example.demo.business.repository.model.JobDAO;
import com.example.demo.model.Employee;
import com.example.demo.model.Job;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.JobService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
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
public class EmployeeControllerIT {
    public static String URL = "http://localhost:8080/api";
    @Autowired
    private MockMvc mockMvc;
    private Employee employee = createEmploye();
    private EmployeeDAO employeeDAO = createEmployee();
    private List<EmployeeDAO> employeeDAOList1 = createEmployeeList();
    private List<Employee> employeeList = createEmployeesList();
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
    @Test
    void testFindAllEmployeesRepository() throws Exception {
        when(employeeRepository.findAll()).thenReturn(employeeList);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("test"))
                .andExpect(status().isOk());
        verify(employeeRepository, times(2)).findAll();
    }
    @Test
    void testFindEmployeeById() throws Exception{
        when(employeeRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(employee));
        mockMvc.perform(MockMvcRequestBuilders.get(URL+"/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value("test"))
                .andExpect(status().isOk());
        verify(employeeRepository,times(1)).findById(1L);
    }
    @Test
    void testSaveEmployee() throws Exception{
        when(employeeRepository.save(employee)).thenReturn(employee);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL + "/add")
                        .content(asJsonString(employeeDAO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        verify(employeeRepository, times(1)).save(employee);
    }
    @Test
    void testDeleteEmployeeById() throws Exception{
        Employee employee1 = createEmploye();
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee1));
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/delete/1")
                .content(asJsonString(employee1))
                .contentType(MediaType.APPLICATION_JSON).
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isNoContent());
        verify(employeeRepository, times(1)).deleteById(anyLong());

    }

    private EmployeeDAO createEmployee() {
        EmployeeDAO employee = new EmployeeDAO();
        employee.setId(1L);
        employee.setName("test");
        employee.setSurname("test");
        employee.setDob("test");
        employee.setJobId(1L);
        return employee;
    }

    private List<EmployeeDAO> createEmployeeList() {
        List<EmployeeDAO> list = new ArrayList<>();
        EmployeeDAO employee1 = createEmployee();
        EmployeeDAO employee2 = createEmployee();
        list.add(employee1);
        list.add(employee2);
        return list;
    }

    private Employee createEmploye() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("test");
        employee.setSurname("test");
        employee.setDob("test");
        employee.setJob(createJob());
        return employee;
    }
    private List<Employee> createEmployeesList(){
        List<Employee> list = new ArrayList<>();
        Employee employee1 = createEmploye();
        Employee employee2 = createEmploye();
        list.add(createEmploye());
        list.add(createEmploye());
        return list;
    }
    private JobDAO createJobDao(){
        JobDAO jobDAO = new JobDAO();
        jobDAO.setId(1L);
        jobDAO.setTitle("test");
        jobDAO.setSalary("test");
        return jobDAO;
    }
    private Job createJob(){
        Job job = new Job();
        job.setId(1L);
        job.setTitle("test");
        job.setSalary("test");
        return job;
    }
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}





