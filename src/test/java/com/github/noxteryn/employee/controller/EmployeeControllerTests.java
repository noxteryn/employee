package com.github.noxteryn.employee.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.noxteryn.employee.exception.EmployeeNotFoundException;
import com.github.noxteryn.employee.model.Employee;
import com.github.noxteryn.employee.repository.EmployeeRepository;
import com.github.noxteryn.employee.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.hamcrest.CoreMatchers.is;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests
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
				.id(1L)
				.firstName("Chris")
				.lastName("Fujikawa")
				.birthDate(LocalDate.of(1986, 3, 18))
				.email("noxteryn@employee.com")
				.socialSecurity(987654321)
				.build();
	}



	// =============== Get Tests ===============
	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_GetAll_200() throws Exception
	{
		mvc.perform(get("/employee"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json("[]"));
		verify(employeeService, times(1)).getAllEmployees(null);
	}
	@Test
	public void test_GetWithoutLogin_401() throws Exception
	{
		mvc.perform(get("/employee"))
				.andExpect(status().isUnauthorized());
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_GetById_200() throws Exception
	{
		Long id = 1L;
		Employee employee = createEmployee();
		when(employeeService.getEmployeeById(id)).thenReturn(employee);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String requestBody = objectMapper.writeValueAsString(employee);

		mvc.perform(get("/employee/{id}", id))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().json(requestBody));
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_GetById_404() throws Exception
	{
		Long id = 1L;
		when(employeeService.getEmployeeById(anyLong())).thenThrow(EmployeeNotFoundException.class);
		mvc.perform(get("/employee/{id}", id))
				.andExpect(status().isNotFound());
	}



	// =============== Post Tests ===============
	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_Post_201() throws Exception
	{
		Employee employee = createEmployee();
		given(employeeService.newEmployee(any(Employee.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String requestBody = objectMapper.writeValueAsString(employee);

		mvc.perform(post("/employee")
				.with(csrf().asHeader()) // CSRF token is required.
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isCreated());
	}
	@Test
	public void test_PostWithoutLogin_401() throws Exception
	{
		Employee employee = createEmployee();
		given(employeeService.newEmployee(any(Employee.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String requestBody = objectMapper.writeValueAsString(employee);

		mvc.perform(post("/employee")
				.with(csrf().asHeader()) // CSRF token is required.
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isUnauthorized());
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_PostWithoutCSRFToken_403() throws Exception
	{
		Employee employee = createEmployee();
		given(employeeService.newEmployee(any(Employee.class)))
				.willAnswer((invocation)-> invocation.getArgument(0));
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String requestBody = objectMapper.writeValueAsString(employee);

		mvc.perform(post("/employee")
				.with(csrf().useInvalidToken()) // Deliberately using an invalid CSRF token.
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isForbidden());
	}



	// =============== Put Tests ===============
	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_Put_200() throws Exception
	{
		Long id = 1L;
		Employee oldEmployee = createEmployee();
		Employee newEmployee = createEmployee();
		newEmployee.setLastName("Filippidis");
		given(employeeService.getEmployeeById(id)).willReturn(oldEmployee);
		given(employeeService.updateEmployee(eq(id), eq(oldEmployee), any(Employee.class)))
				.willReturn(ResponseEntity.ok(newEmployee));
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String requestBody = objectMapper.writeValueAsString(newEmployee);

		mvc.perform(put("/employee/{id}", id)
				.with(csrf().asHeader()) // CSRF token is required.
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.firstName", is(newEmployee.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(newEmployee.getLastName())))
				.andExpect(jsonPath("$.birthDate", is(newEmployee.getBirthDate().toString())))
				.andExpect(jsonPath("$.email", is(newEmployee.getEmail())))
				.andExpect(jsonPath("$.socialSecurity", is(newEmployee.getSocialSecurity())));
	}
	@Test
	public void test_PutWithoutLogin_401() throws Exception
	{
		Long id = 1L;
		Employee oldEmployee = createEmployee();
		Employee newEmployee = createEmployee();
		newEmployee.setLastName("Filippidis");
		given(employeeService.getEmployeeById(id)).willReturn(oldEmployee);
		given(employeeService.updateEmployee(eq(id), eq(oldEmployee), any(Employee.class)))
				.willReturn(ResponseEntity.ok(newEmployee));
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String requestBody = objectMapper.writeValueAsString(newEmployee);

		mvc.perform(put("/employee/{id}", id)
						.with(csrf().asHeader()) // CSRF token is required.
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isUnauthorized());
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_PutWithoutCSRFToken_403() throws Exception
	{
		Long id = 1L;
		Employee oldEmployee = createEmployee();
		Employee newEmployee = createEmployee();
		newEmployee.setLastName("Filippidis");
		given(employeeService.getEmployeeById(id)).willReturn(oldEmployee);
		given(employeeService.updateEmployee(eq(id), eq(oldEmployee), any(Employee.class)))
				.willReturn(ResponseEntity.ok(newEmployee));
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		String requestBody = objectMapper.writeValueAsString(newEmployee);

		mvc.perform(put("/employee/{id}", id)
						.with(csrf().useInvalidToken()) // Deliberately using an invalid CSRF token.
						.contentType(MediaType.APPLICATION_JSON)
						.content(requestBody))
				.andExpect(status().isForbidden());
	}



	// =============== Delete Tests ===============
	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_Delete_204() throws Exception
	{
		Long id = 1L;
		when(employeeService.deleteEmployeeById(id)).thenReturn(ResponseEntity.noContent().build());

		mvc.perform(delete("/employee/{id}", id)
				.with(csrf().asHeader()) // CSRF token is required.
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		verify(employeeService, times(1)).deleteEmployeeById(id);
	}
	@Test
	public void test_DeleteWithoutLogin_401() throws Exception
	{
		Long id = 1L;
		when(employeeService.deleteEmployeeById(id)).thenReturn(ResponseEntity.noContent().build());

		mvc.perform(delete("/employee/{id}", id)
				.with(csrf().asHeader()) // CSRF token is required.
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_DeleteWithoutCSRFToken_403() throws Exception
	{
		Long id = 1L;
		when(employeeService.deleteEmployeeById(id)).thenReturn(ResponseEntity.noContent().build());

		mvc.perform(delete("/employee/{id}", id)
				.with(csrf().useInvalidToken()) // Deliberately using an invalid CSRF token.
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}
	@Test
	@WithMockUser(username = "TestUser", roles = {"USER"})
	public void test_Delete_404() throws Exception
	{
		Long id = 1L;
		doThrow(EmployeeNotFoundException.class).when(employeeService).deleteEmployeeById(id);
		mvc.perform(delete("/employee/{id}", id)
				.with(csrf().asHeader())) // CSRF token is required.
				.andExpect(status().isNotFound());
	}
}
