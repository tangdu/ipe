package com.ipe.module.exl;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ipe.common.exception.CustException;

/**
 * 生成Excel
 * 
 * @author tangdu
 *
 */
public class ExcelCreate extends SimpleExcelCreate {

	private ExcelCreate() {
	}
	
	static ExcelCreate _INSTANCE=null;
	
	public static ExcelCreate getInstance(){
		return _INSTANCE==null ?  _INSTANCE=new ExcelCreate() :_INSTANCE;
	}

	public void createDefault(List<Map<String, Object>> data, List<TableInfo> info,
			OutputStream stream) throws CustException {
		if (info != null && stream != null) {
			try {
				XSSFWorkbook workbook = newBook();
				// 创建表头
				createTitles(workbook, info);
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

	private void createTitles(XSSFWorkbook workbook, List<TableInfo> info) {
		if (info != null) {
			XSSFSheet sheet = workbook.createSheet();
			XSSFRow row = null;
			XSSFCell cell = null;
			row = sheet.createRow(0);
			int i = 0;
			for (TableInfo t : info) {
				cell = row.createCell(i);
				cell.setCellValue((t.getFieldDesc() == null || "".equals(t
						.getFieldDesc())) ? t.getFieldName() : t.getFieldDesc());
				cell.setCellStyle(titleCell);
				i++;
			}
		}
	}

}
