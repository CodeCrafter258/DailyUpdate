package com.in.kanak.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.in.kanak.common.BaseServiceImpl;
import com.in.kanak.common.UserContext;
import com.in.kanak.dao.RoleDAOInt;
import com.in.kanak.dto.RoleDTO;



@Service
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<RoleDTO, RoleDAOInt> implements RoleServiceInt {


	@Transactional(readOnly = true)
	public RoleDTO findByName(String name, UserContext userContext) {
		return baseDao.findByUniqueKey("name", name, userContext);
	}

}
