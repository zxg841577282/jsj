package com.zxg.office.util;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

import static org.apache.poi.ss.usermodel.BorderStyle.NONE;

/**
 * 作者：WGS
 * 微信：w1150111308
 * Q  Q：1150111308
 * 邮箱：gosse0405@163.com
 */
public class ExcelToHtmlUtil {



	/**
	 * 获取所有合并单元格的范围单元格和过程单元格
	 * @param sheet
	 * @return
	 */
	private static Map<String, String>[] getRowSpanColSpanMap(Sheet sheet) {
		// 存储合并单元格的起始终止坐标，方便些td的合并单元格属性的值
		Map<String, String> map0 = new HashMap<String, String>();
		// 存储合并单元格的经过的坐标，方便判断什么时候截止单元格，继续下一个非合并单元格的读取
		Map<String, String> map1 = new HashMap<String, String>();
		// 获取合并单元格的数量
		int mergedNum = sheet.getNumMergedRegions();
		CellRangeAddress range = null;
		for (int i = 0; i < mergedNum; i++) {
			// 获取每个合并单元格的坐标范围，例如[A1:I1]
			range = sheet.getMergedRegion(i);
			// 获取合并单元格的起始行和列索引（从0开始）
			int topRow = range.getFirstRow();
			int topCol = range.getFirstColumn();
			// 获取合并单元格的终止行和列索引（从0开始）
			int bottomRow = range.getLastRow();
			int bottomCol = range.getLastColumn();
			map0.put(topRow + "," + topCol, bottomRow + "," + bottomCol);
			int tempRow = topRow;
			while (tempRow <= bottomRow) {
				int tempCol = topCol;
				while (tempCol <= bottomCol) {
					map1.put(tempRow + "," + tempCol, "");
					tempCol++;
				}
				tempRow++;
			}
			map1.remove(topRow + "," + topCol);
		}
		Map[] map = { map0, map1 };
		return map;
	}

	/**
	 * 获取单元格数据
	 * @param cell
	 * @return
	 */
	private static String getCellValue(Cell cell) {
		String result = "";
		switch (cell.getCellType()) {
		case NUMERIC:
			result = getNumberValue(cell);
			break;
		case STRING:
			result = cell.getRichStringCellValue().toString();
			break;
		case FORMULA:
			try {
				result = getNumberValue(cell);
			} catch (IllegalStateException e) {
				result = String.valueOf(cell.getRichStringCellValue());
			}
			break;
		case ERROR: //故障
			result = "非法字符";
			break;
		}
		return result;
	}

	/**
	 * 获取日期、数值的数据
	 * @param cell
	 * @return
	 */
	private static String getNumberValue(Cell cell) {
		String result = "";
		String dataFormatString = cell.getCellStyle().getDataFormatString(); // 获取单元格格式内容
		short dataFormat = cell.getCellStyle().getDataFormat(); // 获取单元格格式ID
		if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) { // 判断内置日期时间类型
			if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
				result = DateUtil.parseDateToStr(cell.getDateCellValue(), "HH:mm");
			} else {
				result = DateUtil.parseDateToStr(cell.getDateCellValue(), "yyyy-MM-dd");
			}
		} else if (dataFormat == 31 || dataFormat == 57) { // 根据自定期情况添加ID
			result = DateUtil.parseDateToStr(cell.getDateCellValue(), "yyyy-MM-dd");
		} else {
			// 处理科学计数法
			NumberFormat nf = NumberFormat.getInstance();
			// 是否以逗号隔开, 默认true以逗号隔开,如[123,456,789.128]
			nf.setGroupingUsed(false);
			result = String.valueOf(nf.format(cell.getNumericCellValue()));
		}
		return result;
	}

	/**
	 * XSS xlxs 处理表格样式
	 */
	private static void dealXSSExcelStyle(Workbook wb, Sheet sheet, Cell cell, StringBuilder sb){
		CellStyle cellStyle = cell.getCellStyle();
		if (cellStyle != null) {
			HorizontalAlignment alignment = cellStyle.getAlignment();
			sb.append("valign='"+ convertVerticalAlignToHtml(cellStyle.getVerticalAlignment(), cell)+ "' ");//单元格中内容的垂直排列方式
			XSSFFont xf = ((XSSFCellStyle) cellStyle).getFont();
			String  align = convertAlignToHtml(alignment, cell);
			sb.append("style=\"");
			sb.append("font-size: " + xf.getFontHeight() / 20 + "pt;"); // 字体大小
			if (xf.getBold()) {
				sb.append("font-weight: bold;"); // 字体加粗
			}
			sb.append("text-align:" + align + ";");//表头排版样式
			XSSFColor xc = xf.getXSSFColor();
			if (xc != null && !"".equals(xc)) {
				sb.append("color:#" + xc.getARGBHex().substring(2) + ";"); // 字体颜色
			}
			XSSFColor bgColor = (XSSFColor) cellStyle.getFillForegroundColorColor();
			if (bgColor != null && !"".equals(bgColor)) {
				sb.append("background-color:#" + bgColor.getARGBHex().substring(2) + ";"); // 背景颜色
			} else {
				sb.append("background-color:#FFFFFF;"); // 背景颜色
			}
			sb.append(getXSSBorderStyle("border-top:" ,cellStyle.getBorderTop(), ((XSSFCellStyle) cellStyle).getTopBorderXSSFColor()));
			sb.append(getXSSBorderStyle("border-right:",cellStyle.getBorderRight(), ((XSSFCellStyle) cellStyle).getRightBorderXSSFColor()));
			sb.append(getXSSBorderStyle("border-bottom:",cellStyle.getBorderBottom(), ((XSSFCellStyle) cellStyle).getBottomBorderXSSFColor()));
			sb.append(getXSSBorderStyle("border-left:",cellStyle.getBorderLeft(), ((XSSFCellStyle) cellStyle).getLeftBorderXSSFColor()));

			sb.append("\" ");
		}
	}

	/**
	 * HSS xls 处理表格样式
	 */
	private static void dealHSSExcelStyle(Workbook wb, Sheet sheet, Cell cell, StringBuilder sb){
		HSSFWorkbook hw = (HSSFWorkbook) wb;

		HSSFCellStyle cellStyle = hw.createCellStyle();
		if (cellStyle != null) {
			HorizontalAlignment alignment = cellStyle.getAlignment();
			sb.append("valign='"+ convertVerticalAlignToHtml(cellStyle.getVerticalAlignment(), cell)+ "' ");//单元格中内容的垂直排列方式

			HSSFFont xf = cellStyle.getFont(hw);
			String  align = convertAlignToHtml(alignment, cell);
			sb.append("style=\"");
			sb.append("font-size: " + xf.getFontHeight() / 20 + "pt;"); // 字体大小
			if (xf.getBold()) {
				sb.append("font-weight: bold;"); // 字体加粗
			}
			sb.append("text-align:" + align + ";");//表头排版样式
			HSSFColor xc = xf.getHSSFColor(hw);
			if (xc != null && !"".equals(xc)) {
				sb.append("color:#" + xc.getHexString().substring(2) + ";"); // 字体颜色
			}
			HSSFColor bgColor =  cellStyle.getFillForegroundColorColor();
			if (bgColor != null && !"".equals(bgColor)) {
				sb.append("background-color:#" + bgColor.getHexString().substring(2) + ";"); // 背景颜色
			} else {
				sb.append("background-color:#FFFFFF;"); // 背景颜色
			}
			sb.append(getHSSBorderStyle("border-top:" ,cellStyle.getBorderTop(),  bgColor));
			sb.append(getHSSBorderStyle("border-right:",cellStyle.getBorderRight(), bgColor));
			sb.append(getHSSBorderStyle("border-bottom:",cellStyle.getBorderBottom(), bgColor));
			sb.append(getHSSBorderStyle("border-left:",cellStyle.getBorderLeft(), bgColor));

			sb.append("\" ");
		}
	}

	/**
	 * 单元格内容的水平对齐方式
	 * @param horizontalAlignment
	 * @param cell
	 * @return
	 */
	private static String convertAlignToHtml(HorizontalAlignment horizontalAlignment, Cell cell) {
		String align = "left";
		switch (horizontalAlignment) {
		case LEFT:
			align = "left";
			break;
		case CENTER:
			align = "center";
			break;
		case RIGHT:
			align = "right";
			break;
		default:
			switch (cell.getCellType()) {
			case NUMERIC:
				align = "right";
				break;
			default:
				align = "left";
				break;
			}
			break;
		}
		return align;
	}

	/**
	 * 单元格中内容的垂直排列方式
	 * @param verticalAlignment
	 * @return
	 */
	private static String convertVerticalAlignToHtml(VerticalAlignment verticalAlignment, Cell cell) {
		String valign = "middle";
		switch (verticalAlignment) {
		case BOTTOM:
			valign = "bottom";
			break;
		case CENTER:
			valign = "center";
			break;
		case TOP:
			valign = "top";
			break;
		default:
			break;
		}
		return valign;
	}

	/**
	 * XSS xlxs 格式 边框样式
	 */
	private static  String getXSSBorderStyle(String borderDirection, BorderStyle borderStyle, XSSFColor xc){
		String result = borderDirection;
		if (borderStyle == NONE) {
			result += "1px solid #D4D4D4;";
		} else {
			String borderColorStr = xc.getARGBHex();//t.getARGBHex();
			borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000" : borderColorStr.substring(2);
			switch (borderStyle) {
			case MEDIUM:
				result += "2px solid ";
				break;
			case THICK:
				result += "3px solid ";
				break;
			case DASHED:
				result += "1px dashed ";
				break;
			case DOTTED:
				result += "1px dotted ";
				break;
			case DOUBLE:
				result += "1px double ";
				break;
			case THIN:
			default:
				result += "1px solid ";
				break;
			}
			result += borderColorStr + ";";
		}
		return result;
	}

	/**
	 * HSS xls 格式 边框样式
	 */
	private static  String getHSSBorderStyle(String borderDirection, BorderStyle borderStyle, HSSFColor xc){
		String result = borderDirection;
		if (borderStyle == NONE) {
			result += "1px solid #D4D4D4;";
		} else {
			String borderColorStr = xc.getHexString();//t.getARGBHex();
			borderColorStr = borderColorStr == null || borderColorStr.length() < 1 ? "#000000" : borderColorStr.substring(2);
			switch (borderStyle) {
			case MEDIUM:
				result += "2px solid ";
				break;
			case THICK:
				result += "3px solid ";
				break;
			case DASHED:
				result += "1px dashed ";
				break;
			case DOTTED:
				result += "1px dotted ";
				break;
			case DOUBLE:
				result += "1px double ";
				break;
			case THIN:
			default:
				result += "1px solid ";
				break;
			}
			result += borderColorStr + ";";
		}
		return result;
	}

	/**
	 * @param file
	 * @return
	 */
	public static String excelToHtml(File file) {
		StringBuilder sb = new StringBuilder();
		try {
			sb.append("<!DOCTYPE html><html><head><meta charset=\"utf-8\"><title>"+ file.getName() +"</title>");
			sb.append("<style>");
			sb.append("*{box-sizing: border-box; padding: 0; margin: 0}");
			// 动画css
			sb.append("#loading{background-color:#000;opacity: .5;height:100%;width:100%;position:fixed;z-index:99999;margin-top:0px;top:0px;}#loading-center-absolute{position:absolute;left:50%;top:50%;height:20px;width:100px;margin-top:-10px;margin-left:-50px;}.object{width:20px;height:20px;background-color:#FFF;-moz-border-radius:50% 50% 50% 50%;-webkit-border-radius:50% 50% 50% 50%;border-radius:50% 50% 50% 50%;margin-right:20px;margin-bottom:20px;position:absolute;}#object_one{-webkit-animation:object 2s linear infinite;animation:object 2s linear infinite;}#object_two{-webkit-animation:object 2s linear infinite -.4s;animation:object 2s linear infinite -.4s;}#object_three{-webkit-animation:object 2s linear infinite -.8s;animation:object 2s linear infinite -.8s;}#object_four{-webkit-animation:object 2s linear infinite -1.2s;animation:object 2s linear infinite -1.2s;}#object_five{-webkit-animation:object 2s linear infinite -1.6s;animation:object 2s linear infinite -1.6s;}@-webkit-keyframes object{0%{left:100px;top:0}80%{left:0;top:0;}85%{left:0;top:-20px;width:20px;height:20px;}90%{width:40px;height:15px;}95%{left:100px;top:-20px;width:20px;height:20px;}100%{left:100px;top:0;}}@keyframes object{0%{left:100px;top:0}80%{left:0;top:0;}85%{left:0;top:-20px;width:20px;height:20px;}90%{width:40px;height:15px;}95%{left:100px;top:-20px;width:20px;height:20px;}100%{left:100px;top:0;}}");
			sb.append("th{background-color: #E8E8E8; border :1px solid #D4D4D4; color: #777777; white-space: nowrap;min-width: 60px}");
			sb.append("table tr th:nth-of-type(1){min-width: 40px}");
			sb.append(".num{background-color: #E8E8E8; text-align: center; font-weight: bold; color: #777777;}");
			sb.append("#overflow td{cursor: default}");
			sb.append("</style>");
			sb.append("</head><body style=\"overflow:hidden;\">");
			// 加载动画
			sb.append("<div id=\"loading\"><div id=\"loading-center\"><div id=\"loading-center-absolute\"><div class=\"object\" id=\"object_one\"></div><div class=\"object\" id=\"object_two\"></div><div class=\"object\" id=\"object_three\"></div><div class=\"object\" id=\"object_four\"></div></div></div></div>");
			// 内容
			sb.append(getExcelInfo(file));
			// js
			sb.append("<script src=\"/js/jquery-3.4.1.min.js\" ></script>");
			sb.append("<script src=\"/js/excel.js\" charset=\"utf-8\"></script>");
			sb.append("</body></html>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 获取Excel信息
	 * @return
	 */
	private static String getExcelInfo(File file) throws IOException {
		StringBuilder sb = new StringBuilder();

		FileInputStream inputStream = new FileInputStream(file);

		Workbook wb;

		String name = file.getName();
		String suffix = name.substring(name.lastIndexOf("."));
		if(suffix.equals(".xls")){
			wb = new HSSFWorkbook(inputStream);
		}else {
			wb = new XSSFWorkbook(inputStream);
		}

		Sheet sheet = wb.getSheetAt(0);//获取第一个Sheet的内容
		int lastRowNum = sheet.getLastRowNum();
		Map<String, String> map[] = getRowSpanColSpanMap(sheet);

		List<Short> colNumList = new ArrayList<Short>();
		Row row0 = null;
		for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
			row0 = sheet.getRow(rowNum);
			if (row0 == null) {
				continue;
			}
			colNumList.add(row0.getLastCellNum());
		}
		// 最大列数
		int maxColNum = Collections.max(colNumList)-1;

		sb.append("<table id=\"overflow\" style=\"border-collapse:collapse;\">");
		sb.append("<thead>");
		sb.append("<tr>");
		for (int i = -1; i <= maxColNum; i++) {
			sb.append("<th>");
			if (i == -1) {
				sb.append("&nbsp;");
				continue;
			}
			if (i < 26) {
				sb.append((char)(65 + i));
			} else {
				sb.append("" + (char)(65 + i/26 - 1) + (char)(65 + i%26));
			}
			sb.append("</th>");
		}
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");
		Row row = null;
		Cell cell = null;
		for (int rowNum = sheet.getFirstRowNum(); rowNum <= lastRowNum; rowNum++) {
			row = sheet.getRow(rowNum);
			if (row == null) {
				sb.append("<tr>");
				sb.append("<td class=\"num\" style=\"border: 1px solid #D4D4D4;\">"+ (rowNum+1) +"</td>");
				for (int j = 0; j <= maxColNum; j++) {
					sb.append("<td style=\"border: 1px solid #D4D4D4;\"><nobr>&nbsp;</nobr></td>");
				}
				sb.append("</tr>");
				continue;
			}
			sb.append("<tr>");
			sb.append("<td class=\"num\" style=\"border: 1px solid #D4D4D4;\">"+ (rowNum+1) +"</td>");
			for (int colNum = 0; colNum <= maxColNum; colNum++) {
				cell = row.getCell(colNum);
				if (cell == null) {    //特殊情况 空白的单元格会返回null
					sb.append("<td style=\"border: 1px solid #D4D4D4;\"><nobr>&nbsp;</nobr></td>");
					continue;
				}
				String stringValue = getCellValue(cell);
				if (map[0].containsKey(rowNum + "," + colNum)) {
					String pointString = map[0].get(rowNum + "," + colNum);
					map[0].remove(rowNum + "," + colNum);
					int bottomeRow = Integer.parseInt(pointString.split(",")[0]);
					int bottomeCol = Integer.parseInt(pointString.split(",")[1]);
					int rowSpan = bottomeRow - rowNum + 1;
					int colSpan = bottomeCol - colNum + 1;
					sb.append("<td rowspan= '" + rowSpan + "' colspan= '"+ colSpan + "' ");
				} else if (map[1].containsKey(rowNum + "," + colNum)) {
					map[1].remove(rowNum + "," + colNum);
					continue;
				} else {
					sb.append("<td ");
				}

				//需要样式
				if(suffix.equals(".xls")){
					dealHSSExcelStyle(wb, sheet, cell, sb);//处理单元格样式
				}else {
					dealXSSExcelStyle(wb, sheet, cell, sb);//处理单元格样式
				}

				sb.append("><nobr>");
				if (stringValue == null || "".equals(stringValue.trim())) {
					sb.append("&nbsp;");
				} else {
					// 将ascii码为160的空格转换为html下的空格（ ）
					sb.append(stringValue.replace(String.valueOf((char) 160),"&nbsp;"));
				}
				sb.append("</nobr></td>");
			}
			sb.append("</tr>");
		}
		sb.append("</tbody>");
		sb.append("</table>");
		return sb.toString();
	}
}
