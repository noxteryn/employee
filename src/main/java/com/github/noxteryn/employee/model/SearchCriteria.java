package com.github.noxteryn.employee.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class SearchCriteria
{
	private String key;
	private String operation;
	private Object value;
}
