package com.github.noxteryn.employee.service;

import com.github.noxteryn.employee.model.Employee;
import com.github.noxteryn.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
		return null;
	}

	@Override
	public Employee newEmployee(Employee employee)
	{
		return employeeRepository.save(employee);
	}

	@Override
	public void deleteEmployeeById(Long id)
	{

	}
}
