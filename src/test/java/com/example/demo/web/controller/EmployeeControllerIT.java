package com.example.demo.web.controller;

import com.example.demo.business.mappers.EmployeeMapper;
import com.example.demo.business.repository.EmployeeRepository;
import com.example.demo.business.repository.model.EmployeeDAO;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
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

    private EmployeeDAO employeeDAO1 = createEmployee();
    private List<EmployeeDAO> employeeDAOList1 = createEmployeeList();
    private List<Employee> employeeList = createEmployeesList();
    @Autowired
    EmployeeController employeeController;
    @Autowired
    EmployeeService employeeService;

    @MockBean
    EmployeeRepository employeeRepository;
    @Test
    void testFindAllEmployeesRepository() throws Exception {
        when(employeeRepository.findAll()).thenReturn(employeeList);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/all")
                        .content(new ObjectMapper().writeValueAsString(employeeList)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("test"))
                .andExpect(status().isOk());
        verify(employeeRepository, times(1)).findAll();

    }

    private EmployeeDAO createEmployee() {
        EmployeeDAO employee = new EmployeeDAO();
        employee.setId(1L);
        employee.setName("test");
        employee.setSurname("test");
        employee.setDob("test");
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
}





