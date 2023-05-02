package com.github.noxteryn.employee.service;

import com.github.noxteryn.employee.exception.EmployeeNotFoundException;
import com.github.noxteryn.employee.model.Employee;
import com.github.noxteryn.employee.model.SearchCriteria;
import com.github.noxteryn.employee.repository.EmployeeRepository;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class EmployeeServiceImpl implements EmployeeService
{
	private final EmployeeRepository employeeRepository;
	private final EntityManager entityManager;

	public EmployeeServiceImpl(EmployeeRepository employeeRepository, EntityManager entityManager)
	{
		this.employeeRepository = employeeRepository;
		this.entityManager = entityManager;
	}

	public List<Employee> searchEmployees(List<SearchCriteria> search)
	{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
		Root<Employee> root = query.from(Employee.class);
		Predicate predicate = builder.conjunction();
		UserSearchQueryCriteriaConsumer searchConsumer = new UserSearchQueryCriteriaConsumer(predicate, builder, root);

		search.stream().forEach(searchConsumer);
		predicate = searchConsumer.getPredicate();
		query.where(predicate);

		return entityManager.createQuery(query).getResultList();
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
