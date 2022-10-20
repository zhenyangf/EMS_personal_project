package com.example.demo.web.controller;

import com.example.demo.business.repository.model.EmployeeDAO;
import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class EmployeeControllerTest {
    public static String URL = "http://localhost:8080/api";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeController employeeController;
    @Autowired
    private JobController jobController;
    @MockBean
    private EmployeeService employeeService;

    private EmployeeDAO employeeDAO1 = createEmployee();
    private List<EmployeeDAO> employeeDAOList1 = createEmployeeList();

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testFindAllEmployees() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(employeeDAOList1);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/all"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].surname").value("test"))
                .andExpect(status().isOk());
        verify(employeeService, times(1)).getAllEmployees();

    }

    @Test
    void testFindAllEmployeesInvalid() throws Exception {
        List<EmployeeDAO> employeeDAOList = createEmployeeList();
        employeeDAOList.clear();
        when(employeeService.getAllEmployees()).thenReturn(employeeDAOList);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/all"))
                .andExpect(status().isNotFound());
        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void testFindEmployeById() throws Exception {
        Optional<EmployeeDAO> employeeDAO = Optional.of(createEmployee());
        when(employeeService.getEmployeeById(anyLong())).thenReturn(employeeDAO);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value("test"))
                .andExpect(status().isOk());
        verify(employeeService, times(1)).getEmployeeById(anyLong());

    }

    @Test
    void testFindEmployeByInvalidId() throws Exception {
        when(employeeService.getEmployeeById(anyLong())).thenReturn(Optional.empty());
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get(URL + "/4"))
                .andExpect(status().isNotFound());

        verify(employeeService, times(0)).getEmployeeById(null);

    }

    @Test
    void testSaveEmployee() throws Exception {
        EmployeeDAO employeeDAO = createEmployee();

        Employee employee = new Employee();

        when(employeeService.saveEmployee(employeeDAO)).thenReturn(employee);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .post(URL + "/add")
                        .content(asJsonString(employeeDAO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
        verify(employeeService, times(1)).saveEmployee(employeeDAO);
    }

    @Test
    void testDeleteEmployee() throws Exception {
        Optional<EmployeeDAO> employeeDAO = Optional.of(createEmployee());
        when(employeeService.getEmployeeById(anyLong())).thenReturn(employeeDAO);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete(URL + "/delete/1")
                .content(asJsonString(employeeDAO))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
        verify(employeeService, times(1)).deleteEmployeeById(anyLong());
    }

    @Test
    void testDeleteInvalidEmployee() throws Exception {
        Optional<EmployeeDAO> employeeDAO = Optional.of(createEmployee());
        employeeDAO.get().setId(null);
        when(employeeService.getEmployeeById(null)).thenReturn(employeeDAO);
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/delete/4")
                        .content(asJsonString(employeeDAO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(employeeService, times(0)).deleteEmployeeById(null);
    }

    @Test
    void testUpdateEmployee() throws Exception {
        EmployeeDAO employee = createEmployee();
        Employee employee1 = createEmploye();
        when(employeeService.getEmployeeByIdNoDAO(employee.getId())).thenReturn(employee1);

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/update/1")
                        .content(asJsonString(employee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isCreated());
        verify(employeeService, times(1)).saveEmployee(employee);

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
}
