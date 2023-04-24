package com.github.noxteryn.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Employee Directory
// Build an employee directory application that allows users to manage employee records through REST API calls.
// Users can create, retrieve, update, and delete employee records, and the employee data can be stored in a database.
// You can implement features such as search, sorting, and filtering to make it easy for users to find employees.
// You can also add additional functionalities such as employee photo uploads, role-based access control, and reporting.

@SpringBootApplication
public class EmployeeApplication
{

	public static void main(String[] args)
	{
		SpringApplication.run(EmployeeApplication.class, args);
	}

}
