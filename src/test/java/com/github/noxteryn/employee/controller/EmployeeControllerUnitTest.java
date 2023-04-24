package com.github.noxteryn.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.noxteryn.employee.model.Employee;
import com.github.noxteryn.employee.repository.EmployeeRepository;
import com.github.noxteryn.employee.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerUnitTest
{
	@Autowired
	private MockMvc mvc;
	@Autowired
	@MockBean
	private EmployeeRepository employeeRepository;
	@MockBean
	private EmployeeService employeeService;

	public Employee createEmployee()
	{
		return Employee.builder()
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
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_GetAll_Returns200() throws Exception
	{
		mvc.perform(get("/employee"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("[]"));
		verify(employeeService, times(1)).getAllEmployees();
	}

	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_Post_Returns201() throws Exception
	{
		Employee employee = createEmployee();
		given(employeeService.newEmployee(any(Employee.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		String requestBody = mapper.writeValueAsString(employee);

		mvc.perform(post("/employee")
				.with(csrf().asHeader()) // CSRF token is required.
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_Delete_Returns200() throws Exception
	{
		Long id = 1L;
		willDoNothing().given(employeeService).deleteEmployeeById(id);
		mvc.perform(delete("/employee/{id}", id)
				.with(csrf().asHeader())) // CSRF token is required.
				.andExpect(status().isOk());
	}
}
