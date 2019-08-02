package com.cqupt.pagination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class Page implements Serializable {

	private static final long serialVersionUID = 3886253477440783048L;

	public static final String START = "start";

	public static final String LIMIT = "limit";

	private List root;

	private int totalProperty;

	public Page() {
		this(new ArrayList(), 0);
	}

	public Page(List root, int totalProperty) {
		if (root == null) {
			this.root = new ArrayList();
		} else {
			this.root = root;
		}
		this.totalProperty = totalProperty;
	}

	public List getRoot() {
		return root;
	}

	public void setRoot(List root) {
		this.root = root;
	}

	public Integer getTotalProperty() {
		return totalProperty;
	}

	public void setTotalProperty(Integer totalProperty) {
		this.totalProperty = totalProperty;
	}

}
