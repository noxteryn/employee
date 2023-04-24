package com.github.noxteryn.employee.service;

import com.github.noxteryn.employee.model.Employee;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface EmployeeService
{
	List<Employee> getAllEmployees();
	Employee getEmployeeById(Long id);
	Employee newEmployee(Employee employee);
	ResponseEntity<Void> deleteEmployeeById(Long id);
}
