package com.in.kanak.common;



import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.in.exception.DuplicateRecordException;




public abstract class BaseDAOImpl <T extends BaseDTO>implements BaseDAOInt<T> {
	
	@PersistenceContext
	protected EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/**
	 * Find record by Unique key
	 */
	public T findByUniqueKey(String attribute, Object val, UserContext userContext) {

		Class<T> dtoClass = getDTOClass();
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(dtoClass);
		Root<T> qRoot = cq.from(dtoClass);

		Predicate condition = builder.equal(qRoot.get(attribute), val);

		if (userContext != null && !isZeroNumber(userContext.getOrgId())) {
			Predicate conditionGrp = builder.equal(qRoot.get("orgId"), userContext.getOrgId());
			cq.where(condition, conditionGrp);
		} else {
			cq.where(condition);
		}

		TypedQuery<T> query = entityManager.createQuery(cq);
		List<T> list = query.getResultList();

		T dto = null;

		if (list.size() > 0) {
			dto = list.get(0);
		}

		return dto;
	}

	public T findByPK(long pk, UserContext userContext) {
		T dto = entityManager.find(getDTOClass(), pk);
		return dto;
	}

	protected TypedQuery<T> createCriteria(T dto, UserContext userContext) {

		
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> cq = builder.createQuery(getDTOClass());
		Root<T> qRoot = cq.from(getDTOClass());

		cq.select(qRoot);

		List<Predicate> whereClause = getWhereClause(dto, builder, qRoot);

		if (dto.isGroupFilter()) {
			whereClause.add(builder.equal(qRoot.get("orgId"), dto.getOrgId()));
		}
		cq.where(whereClause.toArray(new Predicate[whereClause.size()]));

		List<Order> orderBys = getOrderByClause(dto, builder, qRoot);

		cq.orderBy(orderBys.toArray(new Order[orderBys.size()]));

		TypedQuery<T> query = entityManager.createQuery(cq);
		return query;
	}

	protected abstract List<Predicate> getWhereClause(T dto, CriteriaBuilder builder, Root<T> qRoot);

	public List search(T dto, int pageNo, int pageSize, UserContext userContext) {
		System.out.println("BaseDao search run");

		TypedQuery<T> query = createCriteria(dto, userContext);

		if (pageSize > 0) {
			query.setFirstResult(pageNo * pageSize);
			query.setMaxResults(pageSize);
		}

		List list = query.getResultList();
		return list;
	}

	public List search(T dto, UserContext userContext) {
		return search(dto, 0, 0, userContext);
	}

	public List runHQL(String hql, UserContext userContext) {
		Query q = entityManager.createQuery(hql);
		List l = q.getResultList();
		return l;
	}

	public List marksheetMeritList(String hql, UserContext userContext) {
		Query q = entityManager.createQuery(hql);
		q.setFirstResult(0);
		q.setMaxResults(10);
		List l = q.getResultList();
		return l;
	}

	public long add(T dto, UserContext userContext) {

		dto.setCreatedBy(userContext.getLoginId());
		dto.setCreatedDatetime(new Timestamp(new Date().getTime()));
		dto.setModifiedBy(userContext.getLoginId());
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		dto.setOrgId(userContext.getOrgId());
		dto.setOrgName(userContext.getOrgName());

		populate(dto, userContext);
		System.out.println("Dto start ");
		System.out.println(dto);
		System.out.println("before calling persist method in base dao......Kuldeep");
		entityManager.persist(dto);

		return dto.getId();
	}

	protected void populate(T dto, UserContext userContext) {
	}

	public void update(T dto, UserContext userContext) {
		dto.setModifiedBy(userContext.getLoginId());
		dto.setModifiedDatetime(new Timestamp(new Date().getTime()));
		populate(dto, userContext);
		entityManager.merge(dto);
	}

	private void checkDuplicate(T dto, UserContext userContext) {
		LinkedHashMap<String, Object> uniqueKeys = dto.uniqueKeys();
		if (uniqueKeys == null) {
			return;
		}
		uniqueKeys.forEach((key, value) -> {
			T dtoExist = findByUniqueKey(key, value, userContext);
			if (dtoExist != null && dto.getId() != dtoExist.getId()) {
				throw new DuplicateRecordException(key + " already exists");
			}
		});
	}

	public void delete(T dto, UserContext userContext) {
		entityManager.remove(dto);
	}

	public abstract Class<T> getDTOClass();

	protected boolean isEmptyString(String val) {
		return val == null || val.trim().length() == 0;
	}

	protected boolean isZeroNumber(Double val) {
		return val == null || val == 0;
	}

	protected boolean isZeroNumber(Long val) {
		return val == null || val == 0;
	}

	protected boolean isZeroNumber(Integer val) {
		return val == null || val == 0;
	}

	protected boolean isNotNull(Object val) {
		return val != null;
	}

	protected List<Order> getOrderByClause(T dto, CriteriaBuilder builder, Root<T> qRoot) {
		System.out.println("baseDAO in getOrderByClause.......Jayati ");
		LinkedHashMap<String, String> map = dto.orderBY();

		List<Order> orderBys = new ArrayList<Order>();

		map.forEach((key, value) -> {
			if (value.equals("asc")) {
				System.out.println("in if value equals asc");
				orderBys.add(builder.asc(qRoot.get(key)));
			} else {
				orderBys.add(builder.desc(qRoot.get(key)));
			}
		});

		return orderBys;
	}
}



