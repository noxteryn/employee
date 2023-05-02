package com.github.noxteryn.employee.service;

import com.github.noxteryn.employee.model.Employee;
import com.github.noxteryn.employee.model.SearchCriteria;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface EmployeeService
{
	List<Employee> getAllEmployees();
	Employee getEmployeeById(Long id);
	Employee newEmployee(Employee employee);
	ResponseEntity<Void> deleteEmployeeById(Long id);
	ResponseEntity<Employee> updateEmployee(Long id, Employee oldEmployee, Employee newEmployee);
	List<Employee> searchEmployees(List<SearchCriteria> params);
}
