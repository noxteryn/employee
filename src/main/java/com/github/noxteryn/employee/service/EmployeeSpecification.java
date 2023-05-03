package com.github.noxteryn.employee.service;

import com.github.noxteryn.employee.model.*;
import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@AllArgsConstructor
public class EmployeeSpecification implements Specification<Employee>
{
	private SearchCriteria criteria;
	@Override
	public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder)
	{
		if (criteria.getOperation().equalsIgnoreCase(">"))
		{
			return builder.greaterThanOrEqualTo(root.<String> get(criteria.getKey()), criteria.getValue().toString());
		}
		else if (criteria.getOperation().equalsIgnoreCase("<"))
		{
			return builder.lessThanOrEqualTo(root.<String> get(criteria.getKey()), criteria.getValue().toString());
		}
		else if (criteria.getOperation().equalsIgnoreCase(":"))
		{
			if (root.get(criteria.getKey()).getJavaType() == String.class)
			{
				return builder.like(builder.lower(root.<String>get(criteria.getKey())), "%" + criteria.getValue().toString().toLowerCase() + "%");
			}
			else
			{
				return builder.equal(root.get(criteria.getKey()), criteria.getValue());
			}
		}
		return null;
	}
}
