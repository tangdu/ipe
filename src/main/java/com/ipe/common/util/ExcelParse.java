package com.ipe.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ipe.common.exception.CustException;
import com.ipe.common.exception.Exceptions;

/**
 * Created by tangdu on 14-2-28.
 */
public class ExcelParse {
	private XSSFWorkbook wb;
	private int sheetCot = 0;
	static SimpleDateFormat DATEFORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss", Locale.getDefault());// only support this

	public ExcelParse(String file) throws CustException {
		try {
			InputStream inputStream = new FileInputStream(new File(file));
			wb = new XSSFWorkbook(inputStream);
			sheetCot = wb.getNumberOfSheets();
		} catch (Exception e) {
			e.printStackTrace();
			throw Exceptions.throwCustException(e);
		}
	}

	public ExcelParse(File file) throws CustException {
		try {
			InputStream inputStream = new FileInputStream(file);
			wb = new XSSFWorkbook(inputStream);
			sheetCot = wb.getNumberOfSheets();
		} catch (Exception e) {
			e.printStackTrace();
			throw Exceptions.throwCustException(e);
		}
	}

	public Map<Integer, String> sheet() {
		Map<Integer, String> sheets = new HashMap<Integer, String>();
		for (int i = 0; i < wb.getNumberOfSheets(); i++) {
			sheets.put(i, wb.getSheetName(i));
		}
		return sheets;
	}

	public ArrayList<Object[]> read(Integer sheet) {
		return read(sheet, 1, 1);
	}

	/**
	 * 
	 * @param sheet
	 * @param startRow
	 *            开始行
	 * @param startCol
	 *            开始列
	 * @return
	 */
	public ArrayList<Object[]> read(Integer sheet, Integer startRow,
			Integer startCol) {
		if (sheet == null) {
			sheet = 1;
		}
		if (startRow == null) {
			startRow = 1;
		}
		if (startCol == null) {
			startCol = 1;
		}

		ArrayList<Object[]> rowList = new ArrayList<Object[]>();
		if (wb != null) {
			XSSFSheet sheetAt = wb.getSheetAt(sheet - 1);
			int rowCounts = sheetAt.getPhysicalNumberOfRows();
			for (int i = 0; i < rowCounts; i++) {
				XSSFRow hssfRow = sheetAt.getRow(i);
				if (hssfRow == null) {
					continue;
				}
				if ((i + 1) >= startRow) {
					int cellCount = hssfRow.getPhysicalNumberOfCells();
					Object[] aCells = new Object[cellCount];
					for (int j = 0; j < cellCount; j++) {
						XSSFCell hssfCell = hssfRow.getCell(j);
						aCells[j] = getCell(hssfCell);
					}
					rowList.add(aCells);
				}
			}
		}
		return rowList;
	}

	public Object getCell(XSSFCell cell) {
		if (cell == null)
			return "";
		switch (cell.getCellType()) {
		case XSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				return DATEFORMAT.format(cell.getDateCellValue());
			}
			System.out.println(cell.getNumericCellValue());
			return cell.getNumericCellValue();
		case XSSFCell.CELL_TYPE_STRING:
			return cell.getStringCellValue();
		case XSSFCell.CELL_TYPE_FORMULA:
			return cell.getCellFormula();
		case XSSFCell.CELL_TYPE_BLANK:
			return "";
		case XSSFCell.CELL_TYPE_BOOLEAN:
			return cell.getBooleanCellValue();
		case XSSFCell.CELL_TYPE_ERROR:
			System.out.println(cell.getErrorCellValue());
			return  "";
		}
		return "";
	}

	public static void main(String[] arags) {
		try {
			ExcelParse excelParse = new ExcelParse(
					"F:\\systemp\\userdesktop\\333.xlsx");
			ArrayList<Object[]> data = excelParse.read(3);
			for (Object[] aa : data) {
				for (Object a : aa) {
					System.out.print(a + "#-----");
				}
				System.out.println();
			}
		} catch (CustException e) {
			e.printStackTrace();
		}
	}
}
