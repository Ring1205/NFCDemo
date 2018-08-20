package com.zxycloud.hzy_xg.utils;

import java.util.regex.Pattern;

public enum RegularExpression {
	UUID("/^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$/"), PHONE(
			"^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$"), MAIL(
					"^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"), USERNAME(
							"^[a-zA-Z0-9]\\w{5,20}$");
	private String code;

	RegularExpression(String code) {
		this.code = code;
	}

	public Boolean check(String checkTest) {
		return Pattern.matches(code, checkTest);
	}

}
