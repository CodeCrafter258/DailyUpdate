package com.in.kanak.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.in.kanak.common.BaseForm;

public class LoginForm  extends BaseForm{
	
	@NotEmpty(message= "loginId is required")
	@Email
	private String loginId;

	//please enter password
	@NotEmpty(message= "password is required")
	private String password;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}



