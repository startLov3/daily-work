package com.common.tool.config.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelCopyExample {

    public static void main(String[] args) {
        List<String> originalFilePathList = new ArrayList();
        originalFilePathList.add("E:/常用文件/上线/20240110/1704702600103-workbook.xlsx");
        originalFilePathList.add("E:/常用文件/上线/20240110/RPT20240107063042_20_1_.xls");
        originalFilePathList.add("E:/常用文件/上线/20240110/RPT20240107063322_20_1_.xls");
        originalFilePathList.add("E:/常用文件/上线/20240110/RPT20240107063810_2_1_.xls");

        FileInputStream originalFile = null;
        Workbook originalWorkbook;
        // 创建新的Excel文件
        Workbook newWorkbook = new XSSFWorkbook(); // 或者使用HSSFWorkbook如果需要支持.xls格式

        try {

            for (int i = 0; i < originalFilePathList.size(); i++) {

                String originalFilePath = originalFilePathList.get(i);

                // 读取原始Excel文件
                originalFile = new FileInputStream(originalFilePath);

                /*if (originalFilePath.endsWith(".xls")) {
                    originalWorkbook = new XSSFWorkbook(originalFile);
                } else if (originalFilePath.endsWith(".xlsx")) {
                    originalWorkbook = new XSSFWorkbook(originalFile);//HSSFWorkbook
                } else {
                    throw new IllegalArgumentException("不支持的文件格式");
                }*/

                originalWorkbook = new XSSFWorkbook(originalFile);

                Sheet originalSheet = originalWorkbook.getSheetAt(0);


                Sheet newSheet = newWorkbook.createSheet("Sheet" + i);

                // 复制原始Sheet到新文件（包括内容和样式）
                copySheet(originalSheet, newSheet, newWorkbook);
            }
            // 保存新文件
            try (FileOutputStream fileOut = new FileOutputStream("E:/常用文件/上线/20240110/新文件.xls")) {
                newWorkbook.write(fileOut);
            }

            System.out.println("复制完成");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 在finally块中关闭文件流
            if (originalFile != null) {
                try {
                    originalFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private static void copySheet(Sheet source, Sheet target, Workbook newWorkbook) {
        // 处理合并单元格
        for (int i = 0; i < source.getNumMergedRegions(); i++) {
            CellRangeAddress mergedRegion = source.getMergedRegion(i);
            target.addMergedRegion(mergedRegion);
        }

        // 复制内容和样式
        for (int rowNum = 0; rowNum <= source.getLastRowNum(); rowNum++) {
            Row sourceRow = source.getRow(rowNum);
            Row newRow = target.createRow(rowNum);

            if (sourceRow != null) {
                // 复制每个单元格的内容和样式
                for (int colNum = 0; colNum < sourceRow.getLastCellNum(); colNum++) {
                    Cell sourceCell = sourceRow.getCell(colNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                    Cell newCell = newRow.createCell(colNum);

                    // 复制内容
                    copyCellValue(sourceCell, newCell, newWorkbook);

                    // 复制样式
                    CellStyle newCellStyle = newWorkbook.createCellStyle();
                    newCellStyle.cloneStyleFrom(sourceCell.getCellStyle());
                    newCell.setCellStyle(newCellStyle);

                    // 复制宽度和高度
                    target.setColumnWidth(colNum, source.getColumnWidth(colNum));
                    newRow.setHeight(sourceRow.getHeight());
                }
            }
        }
    }

    private static void copyCellValue(Cell sourceCell, Cell newCell, Workbook newWorkbook) {
        if (sourceCell == null) {
            return;
        }

        DataFormatter dataFormatter = new DataFormatter();
        String formattedValue = dataFormatter.formatCellValue(sourceCell);

        if (sourceCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(sourceCell)) {
            // 处理日期格式
            newCell.setCellValue(sourceCell.getDateCellValue());
        } else {
            newCell.setCellValue(formattedValue);
        }
    }
}