package com.ipe.module.exl;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ipe.common.exception.CustException;

public class SimpleExcelCreate {
	static final Logger LOG = LoggerFactory.getLogger(ExcelCreate.class);
	static SimpleDateFormat DATEFORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat DATEFORMAT2 = new SimpleDateFormat("yyyyMMdd");
	static XSSFCellStyle borderCell;
	static XSSFCellStyle titleCell;
	static XSSFCellStyle linkStyle;
	static Font titleFont;

	protected XSSFWorkbook newBook() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		borderCell = workbook.createCellStyle();
		titleFont = workbook.createFont();
		titleCell = workbook.createCellStyle();

		setBorder(borderCell);
		setBorder(titleCell);
		setFont();
		titleCell.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		titleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		titleCell.setFont(titleFont);
		return workbook;
	}

	private void setFont() {
		titleFont.setFontHeightInPoints((short) 13); // 字体大小
		titleFont.setFontName("黑体");
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // 粗体
		titleFont.setColor(HSSFColor.WHITE.index); // 绿字
	}

	private void setBorder(XSSFCellStyle cellStyle) {
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); // 下边框
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);// 左边框
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);// 上边框
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);// 右边框
	}

	public void createDefault2(List<Map<String, Object>> data,
			List<String> titles, OutputStream stream) throws CustException {
		if (titles != null && stream != null && data!=null) {
			try {
				XSSFWorkbook workbook = newBook();
				//
				if(titles==null||titles.isEmpty()){
					titles=new ArrayList<String>();
					for(Entry<String, Object> en : data.get(1).entrySet()){
						titles.add(en.getKey());
					}
				}
				// 创建表头
				createTitles(workbook, titles);
				// 添加数据
				appendValues(workbook, data);
				// 输出
				workbook.write(stream);
			} catch (Exception e) {
				LOG.error("ERROR ", e);
				throw new CustException(e);
			}
		}
	}

	private void createTitles(XSSFWorkbook workbook, List<String> titles) {
		if (titles != null) {
			XSSFSheet sheet = workbook.createSheet();
			XSSFRow row = null;
			XSSFCell cell = null;
			row = sheet.createRow(0);
			int i = 0;
			for (String t : titles) {
				cell = row.createCell(i);
				cell.setCellValue(t == null ? "" : t);
				cell.setCellStyle(titleCell);
				i++;
			}
		}
	}

	protected void appendValues(XSSFWorkbook workbook,
			List<Map<String, Object>> data) {
		if (data != null) {
			XSSFSheet sheet = workbook.getSheetAt(0);
			XSSFRow row = null;
			XSSFCell cell = null;
			int i = 1;
			int j = 0;
			for (Map<String, Object> _dt : data) {
				row = sheet.createRow(i);
				i++;
				j = 0;
				for (Entry<String, Object> en : _dt.entrySet()) {
					cell = row.createCell(j);
					cell.setCellValue(en.getValue() == null ? "" : en
							.getValue().toString());
					cell.setCellStyle(borderCell);
					j++;
				}
			}
		}
	}
}
