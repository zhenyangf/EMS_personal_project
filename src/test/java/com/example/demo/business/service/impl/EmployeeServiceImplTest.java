package com.example.demo.business.service.impl;

import com.example.demo.business.repository.EmployeeRepository;
import com.example.demo.business.repository.JobRepository;
import com.example.demo.business.repository.model.EmployeeDAO;
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
public class EmployeeServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private JobRepository jobRepository;
    @InjectMocks
    private EmployeeServiceImpl employeeService;
    @InjectMocks
    private JobServiceImpl jobService;

    private EmployeeDAO employeedao;
    private Employee employee;
    private Employee employee2;
    private List<EmployeeDAO> employeeDAOList;
    private List<Employee> employeeList;
    private List<Employee> employeeList1;
    private Job job;
    private Job job1;
    private JobDAO jobDAO;


    @BeforeEach
    public void init() {
        job1 = createJob(2L, "test", "test", employeeList1);
        jobDAO = createJobDao(1L, "test", "test");
        employee = createEmployee(1L, "test2", "test2", "test2", job1);
        employee2 = createEmployee(1L, "test2", "test2", "test2", job1);
        employeeList = createEmployeeList(employee);
        employeeList1 = createEmployeeList1(employee);
        job = createJob(1L, "test", "test", employeeList1);
        employeedao = createEmployeeDao(1L, "test2", "test2", "test2", 2L);
        employeeDAOList = createEmployeeDaoList(employeedao);
    }

    @Test
    void testFindAllEmployees() throws Exception {
        when(employeeRepository.findAll()).thenReturn(employeeList);
        List<EmployeeDAO> employees = employeeService.getAllEmployees();
        assertEquals(2, employees.size());
        verify(employeeRepository, times(2)).findAll();
    }

    @Test
    void testFindAllEmployeesInvalid() throws Exception {
        when(employeeRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(HttpClientErrorException.class, () -> employeeService.getAllEmployees());
    }

    @Test
    void testFindEmployeeById() throws Exception {
        given(employeeRepository.findById(anyLong())).willReturn(Optional.of(employee));

        final Optional<EmployeeDAO> expected = employeeService.getEmployeeById(anyLong());
        assertTrue(expected.isPresent());
        assertEquals(expected, Optional.of(employeedao));
        verify(employeeRepository, times(1)).findById(anyLong());
    }

    @Test
    void testSaveEmployee() throws Exception {
        when(employeeRepository.save(employee)).thenReturn(employee);
        Employee returned = employeeService.saveEmployee(employeedao);
        assertEquals(returned.getName(), employee2.getName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testDeleteEmployeeById() {
        employeeService.deleteEmployeeById(anyLong());
        verify(employeeRepository, times(1)).deleteById(anyLong());
    }


    private EmployeeDAO createEmployeeDao(Long id, String name, String surname, String dob, Long job_id) {
        employeedao = new EmployeeDAO();
        employeedao.setId(id);
        employeedao.setName(name);
        employeedao.setSurname(surname);
        employeedao.setDob(dob);
        employeedao.setJob_id(job_id);
        return employeedao;
    }

    private Employee createEmployee(Long id, String name, String surname, String dob, Job job) {
        employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        employee.setSurname(surname);
        employee.setDob(dob);
        employee.setJob(job);
        return employee;
    }

    private Job createJob(Long id, String title, String salary, List<Employee> employees) {
        job = new Job();
        job.setId(id);
        job.setEmployee(employees);
        job.setTitle(title);
        job.setSalary(salary);
        return job;
    }

    private JobDAO createJobDao(Long id, String title, String salary) {
        jobDAO = new JobDAO();
        jobDAO.setId(id);
        jobDAO.setTitle(title);
        jobDAO.setSalary(salary);
        return jobDAO;
    }

    private List<EmployeeDAO> createEmployeeDaoList(EmployeeDAO employeeDAO) {
        List<EmployeeDAO> list = new ArrayList<>();
        list.add(employeeDAO);
        list.add(employeeDAO);
        return list;
    }

    private List<Employee> createEmployeeList(Employee employee) {
        List<Employee> list = new ArrayList<>();
        list.add(employee);
        list.add(employee);
        return list;
    }

    private List<Employee> createEmployeeList1(Employee employee) {
        List<Employee> list = new ArrayList<>();
        list.add(employee);
        return list;
    }


}
