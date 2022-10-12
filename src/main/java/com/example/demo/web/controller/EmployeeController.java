package com.example.demo.web.controller;

import com.example.demo.business.repository.EmployeeRepository;
import com.example.demo.business.repository.model.EmployeeDAO;
import com.example.demo.business.repository.model.EmployeeUpdate;
import com.example.demo.business.service.EmployeeService;
import com.example.demo.model.Employee;
import com.sun.istack.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

import java.util.List;
import java.util.Optional;
@Api
@Log4j2
@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;
    @Autowired
    EmployeeRepository employeeRepository;


    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDAO>> findAllEmployees() {
        List<EmployeeDAO> employeeList = employeeService.getAllEmployees();
        if (employeeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employeeList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDAO> findEmployeeById(@ApiParam(value = "Employee id", required = true) @NotNull @PathVariable Long id){
        Optional<EmployeeDAO> employee = employeeService.getEmployeeById(id);
        if(!employee.isPresent()){
            log.warn("Employee with id {} not found.",id);
        }
        else{
            log.debug("Employee with id {} is found: {}", id,employee);
        }
        return employee.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody EmployeeDAO employee, BindingResult bindingResult) throws Exception{
        if(bindingResult.hasErrors()){
            log.error("Employee not created: error {}", bindingResult);
            return ResponseEntity.badRequest().build();
        }
            Employee employeeSaved = employeeService.saveEmployee(employee);
            return new ResponseEntity<>(employeeSaved, HttpStatus.CREATED);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployeeById(@PathVariable Long id){
        Optional<EmployeeDAO> employee =employeeService.getEmployeeById(id);
        if (!(employee.isPresent())) {
            log.warn("Employee for delete with id {} is not found.", id);
            return ResponseEntity.notFound().build();}
        employeeService.deleteEmployeeById(id);
        log.debug("Employee with id {} is deleted: {}", id, employee);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDAO employee,BindingResult bindingResult){
        if (bindingResult.hasErrors() || !id.equals(employee.getId())) {
            log.warn("Employee for update with id {} not found", id);
            return ResponseEntity.notFound().build();
        }
        employeeService.saveEmployee(employee);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }


}
