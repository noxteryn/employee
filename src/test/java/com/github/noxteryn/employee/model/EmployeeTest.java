package com.github.noxteryn.employee.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class EmployeeTest
{

	public Employee createEmployee()
	{
		return Employee.builder()
				.id(1L)
				.firstName("Chris")
				.lastName("Fujikawa")
				.birthDate(LocalDate.of(1986, 3, 18))
				.email("noxteryn@employee.com")
				.socialSecurity(123456789)
				.build();
	}
	@Test
	public void test_Employee()
	{
		Employee employee = createEmployee();
		assertThat(employee.getFirstName()).isEqualTo("Chris");
		assertThat(employee.getLastName()).isEqualTo("Fujikawa");
		assertThat(employee.getBirthDate()).isEqualTo("1986-03-18");
		assertThat(employee.getEmail()).isEqualTo("noxteryn@employee.com");
		assertThat(employee.getSocialSecurity()).isEqualTo(123456789);
	}
}
