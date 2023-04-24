package com.github.noxteryn.employee.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class EmployeeUnitTest
{

	public Employee createEmployee()
	{
		return Employee.builder()
				.id(1L)
				.firstName("Chris")
				.lastName("Fujikawa")
				.birthDate(LocalDate.of(1986, 3, 18))
				.email("noxteryn@employee.com")
				.socialSecurity(123)
				.startDate(LocalDate.of(2023, 4, 24))
				.title("Software Developer")
				.build();
	}
	@Test
	public void test_Employee() throws Exception
	{
		Employee employee = createEmployee();
		assertThat(employee.getFirstName()).isEqualTo("Chris");
		assertThat(employee.getLastName()).isEqualTo("Fujikawa");
		assertThat(employee.getBirthDate()).isEqualTo(LocalDate.of(1986, 3, 18));
		assertThat(employee.getEmail()).isEqualTo("noxteryn@employee.com");
		assertThat(employee.getSocialSecurity()).isEqualTo(123);
		assertThat(employee.getStartDate()).isEqualTo(LocalDate.of(2023, 4, 24));
		assertThat(employee.getTitle()).isEqualTo("Software Developer");
	}
}
