package com.github.noxteryn.employee.exception;

public class EmployeeNotFoundException extends RuntimeException
{
	public EmployeeNotFoundException(String exception)
	{
		super (exception);
	}
}
