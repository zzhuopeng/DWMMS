package com.cqupt.map;

import com.cqupt.constant.CriteriaConstant;

import java.util.LinkedHashMap;


@SuppressWarnings("serial")
public class OrderMap extends LinkedHashMap<String, String> {

	/**
	 * 顺序
	 * 
	 * @param key
	 */
	public void asc(String key) {
		this.put(key, CriteriaConstant.ASC);
	}

	/**
	 * 倒序
	 * 
	 * @param key
	 */
	public void desc(String key) {
		this.put(key, CriteriaConstant.DESC);
	}

}
