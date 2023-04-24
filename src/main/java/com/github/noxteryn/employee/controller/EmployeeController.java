package com.github.noxteryn.employee.controller;

import com.github.noxteryn.employee.exception.EmployeeNotFoundException;
import com.github.noxteryn.employee.model.Employee;
import com.github.noxteryn.employee.repository.EmployeeRepository;
import com.github.noxteryn.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
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

	@PostMapping("/employee")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee postEmployee(@RequestBody Employee employee)
	{
		return employeeService.newEmployee(employee);
	}

	@DeleteMapping("/employee/{id}")
	@ResponseStatus(HttpStatus.OK)
	public void delEmployee(@PathVariable Long id)
	{
		try
		{
			employeeService.deleteEmployeeById(id);
		}
		catch (EmployeeNotFoundException exception)
		{
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Invalid ID. Employee not found.");
		}
	}
}
