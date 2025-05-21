package com.in.kanak.service;

import com.in.kanak.common.BaseServiceInt;
import com.in.kanak.common.UserContext;
import com.in.kanak.dto.RoleDTO;

public interface RoleServiceInt extends BaseServiceInt<RoleDTO> {

	/**
	 * Finds Role by name.
	 * 
	 * @param name
	 * @return
	 */
	public RoleDTO findByName(String name, UserContext userContext);

}
