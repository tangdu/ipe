package com.ipe.module.core.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.ipe.common.entity.IDEntity;
import com.ipe.common.util.Constants;

/**
 * Created by tangdu on 14-2-28.
 */
@Entity
@Table(name = "t_exl_imptpl", schema = "", catalog =Constants.SCHEMA)
@JsonIgnoreProperties(value = { "detailesSet" })
public class ExlImptpl extends IDEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6378060585891154095L;
	private String exlCode;
	private String exlName;
	private Timestamp createdDate;
	private String remark;
	private String enabled;
	private String mappingTable;
	private Integer sheetIndex;
	private Integer startrowIndex;
	private Integer startcolIndex;
	private String exlFile;

	@Column(name = "exl_code", nullable = false, insertable = true, updatable = true, length = 50, precision = 0)
	public String getExlCode() {
		return exlCode;
	}

	public void setExlCode(String exlCode) {
		this.exlCode = exlCode;
	}

	@Column(name = "exl_name", nullable = false, insertable = true, updatable = true, length = 100, precision = 0)
	public String getExlName() {
		return exlName;
	}

	public void setExlName(String exlName) {
		this.exlName = exlName;
	}

	@Column(name = "created_date", nullable = true, insertable = true, updatable = true, length = 19, precision = 0)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Timestamp getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "remark_", nullable = true, insertable = true, updatable = true, length = 200, precision = 0)
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "enabled_", nullable = true, insertable = true, updatable = true, length = 2, precision = 0)
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "mapping_table", nullable = false, insertable = true, updatable = true, length = 50, precision = 0)
	public String getMappingTable() {
		return mappingTable;
	}

	public void setMappingTable(String mappingTable) {
		this.mappingTable = mappingTable;
	}

	@Column(name = "sheet_index", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
	public Integer getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(Integer sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	@Column(name = "startrow_index", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
	public Integer getStartrowIndex() {
		return startrowIndex;
	}

	public void setStartrowIndex(Integer startrowIndex) {
		this.startrowIndex = startrowIndex;
	}

	@Column(name = "startcol_index", nullable = true, insertable = true, updatable = true, length = 10, precision = 0)
	public Integer getStartcolIndex() {
		return startcolIndex;
	}

	public void setStartcolIndex(Integer startcolIndex) {
		this.startcolIndex = startcolIndex;
	}

	@Column(name = "exl_file")
	public String getExlFile() {
		return exlFile;
	}

	public void setExlFile(String exlFile) {
		this.exlFile = exlFile;
	}

	public String tableCot;

	@Transient
	public String getTableCot() {
		return tableCot;
	}

	public void setTableCot(String tableCot) {
		this.tableCot = tableCot;
	}

	private List<ExlImptplDetailes> detailesSet;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "exlImptpl")
	public List<ExlImptplDetailes> getDetailesSet() {
		return detailesSet;
	}

	public void setDetailesSet(List<ExlImptplDetailes> detailesSet) {
		this.detailesSet = detailesSet;
	}
}
