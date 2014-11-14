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

import com.ipe.common.exception.CustException;

/**
 * 生成Excel
 * @author tangdu
 *
 */
public class ExcelCreate {
	static SimpleDateFormat DATEFORMAT=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat DATEFORMAT2=new SimpleDateFormat("yyyyMMdd");
	static XSSFCellStyle borderCell;
	static XSSFCellStyle titleCell;
	static XSSFCellStyle linkStyle;
	static Font  titleFont;
	private ExcelCreate(){}
	
	private static XSSFWorkbook newBook(){
		XSSFWorkbook workbook=new XSSFWorkbook();
		borderCell = workbook.createCellStyle();
		titleFont= workbook.createFont();
		titleCell=workbook.createCellStyle(); 
		
		setBorder(borderCell);
		setBorder(titleCell);
		setFont();
		titleCell.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		titleCell.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		titleCell.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中 
		titleCell.setFont(titleFont);
		return workbook;
	}
	
	private static void setFont(){
		titleFont.setFontHeightInPoints((short)13); //字体大小
		titleFont.setFontName("黑体");
		titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD); //粗体
		titleFont.setColor(HSSFColor.WHITE.index);    //绿字
	}
	
	private static void setBorder(XSSFCellStyle cellStyle){
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN); //下边框
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);//左边框
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);//上边框
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);//右边框
	}
	
	public static void createDefault2(List<Map<String,Object>> data,List<TableInfo> info,OutputStream stream) throws CustException{
		List<Object[]> dt=new ArrayList<Object[]>();
		if(data!=null){
			for(Map<String,Object>  m:data){
				Object[] dr=new Object[m.size()];
				int i=0;
				for(Entry<String,Object> en:m.entrySet()){
					dr[i]=en.getValue();
				    i++;
				}
				dt.add(dr);
			}
			createDefault1(dt,info,stream);
		}
	}
	
	public static void createDefault1(List<Object[]> data,List<TableInfo> info,OutputStream stream) throws CustException{
		if(info!=null && stream!=null){
			try {
				XSSFWorkbook workbook=newBook();
				//创建表头
				createTitles(workbook,info);
				//添加数据
				appendValues(workbook,data);
				//输出
				workbook.write(stream);
			} catch (Exception e) {
				e.printStackTrace();
				throw new CustException(e);
			} 
		}
	}
	
	private static void createTitles(XSSFWorkbook workbook,List<TableInfo> info){
		if(info!=null){
			XSSFSheet sheet=workbook.createSheet();
			XSSFRow row=null;
			XSSFCell cell=null;
			row=sheet.createRow(0);
			int i=0;
			for(TableInfo  t:info){
				cell=row.createCell(i);
			    cell.setCellValue((t.getFieldDesc()==null||"".equals(t.getFieldDesc()))?t.getFieldName():t.getFieldDesc());
			    cell.setCellStyle(titleCell);
				i++;
			}
		}
	}
	
	private static void appendValues(XSSFWorkbook workbook,List<Object[]> data){
		if(data!=null){
			XSSFSheet sheet=workbook.getSheetAt(0);
			XSSFRow row=null;
			XSSFCell cell=null;
			int i=1;
			for(Object[] dt:data){
				row=sheet.createRow(i);
				i++;
				for(int j=0;j<dt.length;j++){
					cell=row.createCell(j);
					cell.setCellValue(dt[j]==null ? "" :dt[j].toString());
					cell.setCellStyle(borderCell);
				}
			}
		}
	}
}
