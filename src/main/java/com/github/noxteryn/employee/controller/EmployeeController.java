package com.github.noxteryn.employee.controller;

import com.github.noxteryn.employee.exception.EmployeeNotFoundException;
import com.github.noxteryn.employee.model.Employee;
import com.github.noxteryn.employee.repository.EmployeeRepository;
import com.github.noxteryn.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/")
public class EmployeeController
{
	private EmployeeService employeeService;
	private final EmployeeRepository employeeRepository;
	public EmployeeController (EmployeeRepository employeeRepository)
	{
		this.employeeRepository = employeeRepository;
	}
	@Autowired
	public void setEmployeeService(EmployeeService employeeService)
	{
		this.employeeService = employeeService;
	}

	@GetMapping("/employee")
	@ResponseStatus(HttpStatus.OK)
	public List<Employee> getAllEmployees()
	{
		return employeeService.getAllEmployees();
	}

	@GetMapping("/employee/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id)
	{
		Employee employee = employeeService.getEmployeeById(id);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return ResponseEntity.ok().headers(headers).body(employee);
	}

	@GetMapping("/employee/search")
	@ResponseStatus(HttpStatus.OK)
	public List<Employee> searchEmployees(@RequestParam(name = "firstName", required = false) String firstName,
	                                      @RequestParam(name = "lastName", required = false) String lastName,
	                                      @RequestParam(name = "birthDate", required = false) LocalDate birthDate,
	                                      @RequestParam(name = "email", required = false) String email,
	                                      @RequestParam(name = "socialSecurity", required = false) Integer socialSecurity)
	{
		return employeeService.searchEmployees(firstName, lastName, birthDate, email, socialSecurity);
	}

	@PostMapping("/employee")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee postEmployee(@RequestBody Employee employee)
	{
		return employeeService.newEmployee(employee);
	}

	@PutMapping("/employee/{id}")
	public ResponseEntity<Employee> putEmployee(@PathVariable Long id, @RequestBody Employee newEmployee)
	{
		return employeeService.updateEmployee(id, employeeService.getEmployeeById(id), newEmployee);
	}

	@DeleteMapping("/employee/{id}")
	public ResponseEntity<Void> delEmployee(@PathVariable Long id)
	{
		return employeeService.deleteEmployeeById(id);
	}

	@ExceptionHandler(EmployeeNotFoundException.class)
	public ResponseEntity<String> handleEmployeeNotFoundException(EmployeeNotFoundException exception)
	{
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
	}
}
