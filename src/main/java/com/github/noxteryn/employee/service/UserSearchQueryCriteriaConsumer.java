package com.github.noxteryn.employee.service;

import com.github.noxteryn.employee.model.SearchCriteria;
import jakarta.persistence.criteria.*;
import lombok.*;
import java.util.function.Consumer;


@AllArgsConstructor
@Getter
@Setter
public class UserSearchQueryCriteriaConsumer implements Consumer<SearchCriteria>
{
	private Predicate predicate;
	private CriteriaBuilder builder;
	private Root<?> root;

	@Override
	public void accept(SearchCriteria param)
	{
		if (param.getOperation().equalsIgnoreCase(">"))
		{
			predicate = builder.and(predicate, builder.greaterThanOrEqualTo(root.get(param.getKey()), param.getValue().toString()));
		}
		else if (param.getOperation().equalsIgnoreCase("<"))
		{
			predicate = builder.and(predicate, builder.lessThanOrEqualTo(root.get(param.getKey()), param.getValue().toString()));
		}
		else if (param.getOperation().equalsIgnoreCase(":"))
		{
			if (root.get(param.getKey()).getJavaType() == String.class)
			{
				predicate = builder.and(predicate, builder.like(builder.lower(root.get(param.getKey())), "%" + param.getValue().toString().toLowerCase() + "%"));
			}
			else
			{
				predicate = builder.and(predicate, builder.equal(root.get(param.getKey()), param.getValue()));
			}
		}
	}
}
