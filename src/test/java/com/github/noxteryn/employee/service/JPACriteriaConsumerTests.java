package com.github.noxteryn.employee.service;

import com.github.noxteryn.employee.model.Employee;
import com.github.noxteryn.employee.model.SearchCriteria;
import com.github.noxteryn.employee.repository.EmployeeRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.*;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class JPACriteriaConsumerTests
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
		List<SearchCriteria> params = new ArrayList<SearchCriteria>();
		params.add(new SearchCriteria("firstName", ":", "john"));
		params.add(new SearchCriteria("lastName", ":", "doe"));

		List<Employee> results = employeeService.searchEmployees(params);

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
		List<SearchCriteria> params = new ArrayList<SearchCriteria>();
		params.add(new SearchCriteria("lastName", ":", "doe"));

		List<Employee> results = employeeService.searchEmployees(params);
		assertThat(results.size(), is(equalTo(2)));
		assertThat(results.get(0).getFirstName(), is(equalToIgnoringCase("John")));
		assertThat(results.get(0).getLastName(), is(equalToIgnoringCase("Doe")));
		assertThat(results.get(0).getBirthDate(), is(equalTo(LocalDate.of(1986, 3, 18))));
		assertThat(results.get(0).getEmail(), is(equalToIgnoringCase("john@doe.com")));
		assertThat(results.get(0).getSocialSecurity(), is(equalTo(123456789)));
		assertThat(results.get(1).getFirstName(), is(equalToIgnoringCase("Tom")));
		assertThat(results.get(1).getLastName(), is(equalToIgnoringCase("Doe")));
		assertThat(results.get(1).getBirthDate(), is(equalTo(LocalDate.of(1987, 4, 19))));
		assertThat(results.get(1).getEmail(), is(equalToIgnoringCase("tom@doe.com")));
		assertThat(results.get(1).getSocialSecurity(), is(equalTo(987654321)));
	}

	@Test
	public void test_search_EmployeeDoesNotExist() throws Exception
	{
		List<SearchCriteria> params = new ArrayList<SearchCriteria>();
		params.add(new SearchCriteria("firstName", ":", "Adam"));
		params.add(new SearchCriteria("lastName", ":", "Fox"));

		List<Employee> results = employeeService.searchEmployees(params);
		assertThat(results.size(), is(equalTo(0)));
	}

	@Test
	public void test_search_by_Partial() throws Exception
	{
		List<SearchCriteria> params = new ArrayList<SearchCriteria>();
		params.add(new SearchCriteria("firstName", ":", "oh"));

		List<Employee> results = employeeService.searchEmployees(params);

		assertThat(results.size(), is(equalTo(1)));
		assertThat(results.get(0).getFirstName(), is(equalToIgnoringCase("John")));
		assertThat(results.get(0).getLastName(), is(equalToIgnoringCase("Doe")));
		assertThat(results.get(0).getBirthDate(), is(equalTo(LocalDate.of(1986, 3, 18))));
		assertThat(results.get(0).getEmail(), is(equalToIgnoringCase("john@doe.com")));
		assertThat(results.get(0).getSocialSecurity(), is(equalTo(123456789)));
	}
}
