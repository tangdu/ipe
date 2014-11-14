package com.ipe.module.exl.entity;

import java.util.Date;
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
	private Date createdDate;
	private String remark;
	private String enabled;
	private String mappingTable;
	private String tableBelongUser;
	private Integer sheetIndex;
	private Integer startrowIndex;
	private Integer startcolIndex;
	private String exlFile;

	@Column(name="table_belong_user")
	public String getTableBelongUser() {
		return tableBelongUser;
	}

	public void setTableBelongUser(String tableBelongUser) {
		this.tableBelongUser = tableBelongUser;
	}

	@Column(name = "exl_code")
	public String getExlCode() {
		return exlCode;
	}

	public void setExlCode(String exlCode) {
		this.exlCode = exlCode;
	}

	@Column(name = "exl_name")
	public String getExlName() {
		return exlName;
	}

	public void setExlName(String exlName) {
		this.exlName = exlName;
	}

	@Column(name = "created_date", updatable = false)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Column(name = "remark_")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Column(name = "enabled_")
	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	@Column(name = "mapping_table")
	public String getMappingTable() {
		return mappingTable;
	}

	public void setMappingTable(String mappingTable) {
		this.mappingTable = mappingTable;
	}

	@Column(name = "sheet_index")
	public Integer getSheetIndex() {
		return sheetIndex;
	}

	public void setSheetIndex(Integer sheetIndex) {
		this.sheetIndex = sheetIndex;
	}

	@Column(name = "startrow_index")
	public Integer getStartrowIndex() {
		return startrowIndex;
	}

	public void setStartrowIndex(Integer startrowIndex) {
		this.startrowIndex = startrowIndex;
	}

	@Column(name = "startcol_index")
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
