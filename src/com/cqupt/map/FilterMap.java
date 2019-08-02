package com.cqupt.map;

import com.cqupt.constant.BaseConstant;
import com.cqupt.constant.CriteriaConstant;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("serial")
public class FilterMap extends HashMap<String, Object> {

	/**
	 * 等于
	 * 
	 * @param key
	 * @param value
	 */
	public void eq(String key, Object value) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.EQ, value);
	}

	/**
	 * 不等于
	 * 
	 * @param key
	 * @param value
	 */
	public void ne(String key, Object value) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.NE, value);
	}

	/**
	 * 大于
	 * 
	 * @param key
	 * @param value
	 */
	public void gt(String key, Object value) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.GT, value);
	}

	/**
	 * 大于等于
	 * 
	 * @param key
	 * @param value
	 */
	public void ge(String key, Object value) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.GE, value);
	}

	/**
	 * 小于
	 * 
	 * @param key
	 * @param value
	 */
	public void lt(String key, Object value) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.LT, value);
	}

	/**
	 * 小于等于
	 * 
	 * @param key
	 * @param value
	 */
	public void le(String key, Object value) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.LE, value);
	}

	/**
	 * 之间
	 * 
	 * @param key
	 * @param map
	 */
	public void between(String key, Map<String, Object> map) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.BETWEEN, map);
	}

	/**
	 * 相似
	 * 
	 * @param key
	 * @param value
	 */
	public void like(String key, String value) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.LIKE, value);
	}

	/**
	 * 之内
	 * 
	 * @param key
	 * @param value
	 */
	public void in(String key, Object[] value) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.IN, value);
	}

	/**
	 * 空
	 * 
	 * @param key
	 */
	public void isNull(String key) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.ISNULL, null);
	}

	/**
	 * 非空
	 * 
	 * @param key
	 */
	public void isNotNull(String key) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.ISNOTNULL, null);
	}

	/**
	 * 空白
	 * 
	 * @param key
	 */
	public void isEmpty(String key) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.ISEMPTY, null);
	}

	/**
	 * 非空白
	 * 
	 * @param key
	 */
	public void isNotEmpty(String key) {
		this.put(key + BaseConstant.SPLIT + CriteriaConstant.ISNOTEMPTY, null);
	}

	/**
	 * sql
	 * 
	 * @param sql
	 */
	public void sql(String sql) {
		this.put(BaseConstant.SPLIT + CriteriaConstant.SQL, sql);
	}

}
