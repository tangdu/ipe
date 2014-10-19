package com.ipe.common.web.util;

/**
 * ExtJS 4 返回数据格式如下
 * 
 * @author tangdu
 * 
 * @time 2013-5-12 上午10:36:55
 */
public class JsonResponse {

	private boolean success;
	private Object rows;
	private long totalCount;
	private String msg;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Object getRows() {
		return rows;
	}

	public void setRows(Object rows) {
		this.rows = rows;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
