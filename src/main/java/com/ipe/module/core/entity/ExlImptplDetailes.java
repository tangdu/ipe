package com.ipe.module.core.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

/**
 * Created by tangdu on 14-2-28.
 */
@Entity
@Table(name = "t_exl_imptpl_detailes", schema = "", catalog = Constants.SCHEMA)
@JsonIgnoreProperties(value = { "exlImptpl" })
public class ExlImptplDetailes extends IDEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7177444812377620755L;
	private Integer exlCol;
	private String tableCol;
	private String colType;
	private String colDesc;
	private String defValue;

	@Column(name = "exl_col")
	public Integer getExlCol() {
		return exlCol;
	}

	public void setExlCol(Integer exlCol) {
		this.exlCol = exlCol;
	}

	@Column(name = "table_col", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
	public String getTableCol() {
		return tableCol;
	}

	public void setTableCol(String tableCol) {
		this.tableCol = tableCol;
	}

	@Column(name = "col_type", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
	public String getColType() {
		return colType;
	}

	public void setColType(String colType) {
		this.colType = colType;
	}

	@Column(name = "col_desc", nullable = true, insertable = true, updatable = true, length = 50, precision = 0)
	public String getColDesc() {
		return colDesc;
	}

	public void setColDesc(String colDesc) {
		this.colDesc = colDesc;
	}

	@Column(name = "def_value")
	public String getDefValue() {
		return defValue;
	}

	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}

	private ExlImptpl exlImptpl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tpl_id")
	public ExlImptpl getExlImptpl() {
		return exlImptpl;
	}

	public void setExlImptpl(ExlImptpl exlImptpl) {
		this.exlImptpl = exlImptpl;
	}
}
