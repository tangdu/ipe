package com.ipe.common.util;

import java.util.List;

/**
 * 分页model
 * 
 * @author tangdu
 * 
 * @time 2013-1-24 下午7:51:11
 */
public class PageModel {

	private int startRow = -1;
	private int endRow = -1;
	private int pageSize = 5;
	private long totalRows = 0;
	private long totalPages = 0;

	private List<Object> list;

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(long totalRows) {
		this.totalRows = totalRows;
	}

	public long getTotalPages() {
		 totalPages=this.totalRows%2==0 ? this.totalRows/this.pageSize : this.totalRows/this.pageSize+1;
		 return totalPages;
	}

	public void setTotalPages(long totalPages) {
		this.totalPages = totalPages;
	}

	public List<Object> getList() {
		return list;
	}

	public void setList(List<Object> list) {
		this.list = list;
	}
}
