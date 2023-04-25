package com.github.noxteryn.employee.repository;

import com.github.noxteryn.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>
{
	List<Employee> findByFirstName(String firstName);
	List<Employee> findByLastName(String lastName);
	List<Employee> findByBirthDate(LocalDate birthDate);
	List<Employee> findByEmail(String email);
	List<Employee> findBySocialSecurity(int socialSecurity);

}
