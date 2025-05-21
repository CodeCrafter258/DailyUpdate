package com.in.kanak.common.message;

import com.in.kanak.common.BaseServiceInt;
import com.in.kanak.common.UserContext;

/**
 * College Service interface.
 * @authorkanaksoni
 */

public interface MessageServiceInt extends BaseServiceInt<MessageDTO> {

	/**
	 * Finds Role by name.
	 * 
	 * @param name
	 * @return
	 * 
	 */
	public MessageDTO findByTitle(String name, UserContext userContext);

	public MessageDTO findByCode(String code, UserContext userContext);

}
