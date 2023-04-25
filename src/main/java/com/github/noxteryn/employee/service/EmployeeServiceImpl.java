package com.github.noxteryn.employee.service;

import com.github.noxteryn.employee.exception.EmployeeNotFoundException;
import com.github.noxteryn.employee.model.Employee;
import com.github.noxteryn.employee.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
		} else
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
	public ResponseEntity<Employee> updateEmployee(Long id, Employee oldEmployee, Employee newEmployee)
	{
		oldEmployee.setFirstName(newEmployee.getFirstName());
		oldEmployee.setLastName(newEmployee.getLastName());
		oldEmployee.setBirthDate(newEmployee.getBirthDate());
		oldEmployee.setEmail(newEmployee.getEmail());
		oldEmployee.setSocialSecurity(newEmployee.getSocialSecurity());
		return ResponseEntity.ok(employeeRepository.save(oldEmployee));
	}

	@Override
	public ResponseEntity<Void> deleteEmployeeById(Long id)
	{
		Optional<Employee> employee = employeeRepository.findById(id);
		if (employee.isPresent())
		{
			employeeRepository.deleteById(id);
			return ResponseEntity.noContent().build();
		}
		else
		{
			return ResponseEntity.notFound().build();
		}
	}
}
