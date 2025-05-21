package com.in.kanak.common;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.in.exception.DatabaseException;
import com.in.exception.DuplicateRecordException;


public  abstract class BaseServiceImpl < T extends BaseDTO , D extends BaseDAOInt<T>> {
	private static Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);

	@Autowired
	protected D baseDao;

	@Transactional(readOnly = true)
	public T findById(long id, UserContext userContext) {
		return baseDao.findByPK(id, userContext);
	}
	
	@Transactional(readOnly = true)
	public T findByUniqueKey(String att, String val, UserContext userContext) {
		return baseDao.findByUniqueKey(att, val, userContext);
	}

	@Transactional(readOnly = true)
	public List<T> search(T dto, int pageNo, int pageSize, UserContext userContext) {
		System.out.println("Search run in Baseservice......Jayati");
		return baseDao.search(dto, pageNo, pageSize, userContext);
	}

	@Transactional(readOnly = true)
	public List<T> search(T dto, UserContext userContext) {
		System.out.println("baseservice Search run.....Kuldeep ");
		return baseDao.search(dto, userContext);  
	}

	@Transactional
	public long add(T dto, UserContext userContext) throws DuplicateRecordException {
		System.out.println("add() run in BaseSecrvice......Kuldeep ");
		return baseDao.add(dto, userContext);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void update(T dto, UserContext userContext) throws DuplicateRecordException {
		baseDao.update(dto, userContext);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public long save(T dto, UserContext userContext) throws DuplicateRecordException {
		Long id = dto.getId();
		if (id != null && id > 0) {
			update(dto, userContext);
		} else {
			id = add(dto, userContext);
		}
		return id;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public T delete(long id, UserContext userContext) {
		log.debug("Base Service delete Start");
		T dto = findById(id, userContext);
		if (dto == null) {
			throw new DatabaseException("Record not found");
		}
		baseDao.delete(dto, userContext);
		log.debug("Base Service delete End");
		return dto;
	}
}
