package com.github.noxteryn.employee.service;

import com.github.noxteryn.employee.model.Employee;
import com.github.noxteryn.employee.model.SearchCriteria;
import com.github.noxteryn.employee.repository.EmployeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JPASpecificationIntegrationTests
{
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private EmployeeService employeeService;

	private Employee john;
	private Employee tom;

	@Before
	public void initialization()
	{
		john = Employee.builder()
				.id(1L)
				.firstName("John")
				.lastName("Doe")
				.birthDate(LocalDate.of(1986, 3, 18))
				.email("john@doe.com")
				.socialSecurity(123456789)
				.build();
		employeeRepository.saveAndFlush(john);

		tom = Employee.builder()
				.id(2L)
				.firstName("Tom")
				.lastName("Doe")
				.birthDate(LocalDate.of(1987, 4, 19))
				.email("tom@doe.com")
				.socialSecurity(987654321)
				.build();
		employeeRepository.saveAndFlush(tom);
	}

	@Test
	public void test_search_by_FirstAndLastName() throws Exception
	{
		SearchCriteria crit1 = new SearchCriteria("firstName", ":", "john");
		SearchCriteria crit2 = new SearchCriteria("lastName", ":", "doe");
		EmployeeSpecification spec1 = new EmployeeSpecification(crit1);
		EmployeeSpecification spec2 = new EmployeeSpecification(crit2);

		System.out.println(employeeRepository.findAll().toString());
		List<Employee> results = employeeRepository.findAll(Specification.where(spec1).and(spec2));

		assertThat(results.size(), is(equalTo(1)));
		assertThat(results.get(0).getFirstName(), is(equalToIgnoringCase("John")));
		assertThat(results.get(0).getLastName(), is(equalToIgnoringCase("Doe")));
		assertThat(results.get(0).getBirthDate(), is(equalTo(LocalDate.of(1986, 3, 18))));
		assertThat(results.get(0).getEmail(), is(equalToIgnoringCase("john@doe.com")));
		assertThat(results.get(0).getSocialSecurity(), is(equalTo(123456789)));
	}

	@Test
	public void test_search_by_LastName() throws Exception
	{
		SearchCriteria crit = new SearchCriteria("lastName", ":", "doe");
		EmployeeSpecification spec = new EmployeeSpecification(crit);

		List<Employee> results = employeeRepository.findAll(Specification.where(spec));

		assertThat(results.size(), is(equalTo(2)));
		assertThat(results.get(0).getFirstName(), is(equalTo("John")));
		assertThat(results.get(0).getLastName(), is(equalTo("Doe")));
		assertThat(results.get(0).getBirthDate(), is(equalTo(LocalDate.of(1986, 3, 18))));
		assertThat(results.get(0).getEmail(), is(equalTo("john@doe.com")));
		assertThat(results.get(0).getSocialSecurity(), is(equalTo(123456789)));
		assertThat(results.get(1).getFirstName(), is(equalTo("Tom")));
		assertThat(results.get(1).getLastName(), is(equalTo("Doe")));
		assertThat(results.get(1).getBirthDate(), is(equalTo(LocalDate.of(1987, 4, 19))));
		assertThat(results.get(1).getEmail(), is(equalTo("tom@doe.com")));
		assertThat(results.get(1).getSocialSecurity(), is(equalTo(987654321)));
	}

	@Test
	public void test_search_EmployeeDoesNotExist() throws Exception
	{
		SearchCriteria crit1 = new SearchCriteria("firstName", ":", "adam");
		SearchCriteria crit2 = new SearchCriteria("lastName", ":", "fox");
		EmployeeSpecification spec1 = new EmployeeSpecification(crit1);
		EmployeeSpecification spec2 = new EmployeeSpecification(crit2);

		List<Employee> results = employeeRepository.findAll(Specification.where(spec1).and(spec2));

		assertThat(results.size(), is(equalTo(0)));
	}

	@Test
	public void test_search_by_Partial() throws Exception
	{
		SearchCriteria crit = new SearchCriteria("firstName", ":", "oh");
		EmployeeSpecification spec = new EmployeeSpecification(crit);

		List<Employee> results = employeeRepository.findAll(Specification.where(spec));

		assertThat(results.size(), is(equalTo(1)));
		assertThat(results.get(0).getFirstName(), is(equalTo("John")));
		assertThat(results.get(0).getLastName(), is(equalTo("Doe")));
		assertThat(results.get(0).getBirthDate(), is(equalTo(LocalDate.of(1986, 3, 18))));
		assertThat(results.get(0).getEmail(), is(equalTo("john@doe.com")));
		assertThat(results.get(0).getSocialSecurity(), is(equalTo(123456789)));
	}
}
