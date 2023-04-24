package com.github.noxteryn.employee.service;

import com.github.noxteryn.employee.model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeeService
{
	List<Employee> getAllEmployees();
	Employee getEmployeeById(Long id);
	Employee newEmployee(Employee employee);
	void deleteEmployeeById(Long id);
}
