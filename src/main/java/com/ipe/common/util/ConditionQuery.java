package com.ipe.common.util;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

/**
 * hibernate 条件util
 * @author tangdu
 *
 * @time 2013-1-24 下午8:20:47
 */
public class ConditionQuery {
	private List<Criterion> criterions = new ArrayList<Criterion>();
	private List<Order> orders = new ArrayList<Order>();

	public void add(Criterion criterion) {
		criterions.add(criterion);
	}

	public void buildConditions(Criteria criteria) {
		for (Criterion criterion2 : criterions) {
			criteria.add(criterion2);
		}
	}

	public void add(Order order) {
		orders.add(order);
	}

	public void buildOrders(Criteria criteria) {
		for (Order order : orders) {
			criteria.addOrder(order);
		}
	}
}
