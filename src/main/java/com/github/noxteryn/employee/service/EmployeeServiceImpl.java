package com.github.noxteryn.employee.service;

import com.github.noxteryn.employee.exception.EmployeeNotFoundException;
import com.github.noxteryn.employee.model.Employee;
import com.github.noxteryn.employee.repository.EmployeeRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService
{
	private final EmployeeRepository employeeRepository;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository)
	{
		this.employeeRepository = employeeRepository;
	}

	@Override
	public List<Employee> getAllEmployees()
	{
		return employeeRepository.findAll();
	}

	@Override
	public Employee getEmployeeById(Long id)
	{
		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isPresent())
		{
			return employee.get();
		}
		else
		{
			throw new EmployeeNotFoundException("Invalid ID. Employee not found.");
		}
	}

	@Override
	public Employee newEmployee(Employee employee)
	{
		return employeeRepository.save(employee);
	}

	@Override
	public void deleteEmployeeById(Long id)
	{
		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isPresent())
		{
			employeeRepository.deleteById(id);
		}
		else
		{
			throw new EmployeeNotFoundException("Invalid ID. Employee not found.");
		}
	}
}
