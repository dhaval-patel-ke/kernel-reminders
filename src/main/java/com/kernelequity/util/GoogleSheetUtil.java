package com.kernelequity.util;

import com.kernelequity.BuildConfig;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class GoogleSheetUtil {

    public static Sheet readSheet(int index) {
        try {
            Workbook workbook = new XSSFWorkbook(Files.newInputStream(Paths.get(BuildConfig.DATA_PATH)));
            return workbook.getSheetAt(index);
        } catch (IOException e) {
            System.out.println("Failed to read sheet from " + BuildConfig.DATA_PATH + " :" + e.getMessage());
            System.exit(-1);
        }
        return null;
    }

    public void download(String sheetUrl, String saveDir) {
        try {
            URL url = new URL(sheetUrl);
            InputStream inputStream = new BufferedInputStream(url.openStream());
            FileOutputStream fileOS = new FileOutputStream(saveDir);
            byte[] data = new byte[8 * 1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                fileOS.write(data, 0, byteContent);
            }
            inputStream.close();
            fileOS.close();
        } catch (IOException e) {
            System.out.println("Download failed: " + e.getMessage());
        }
    }

    public static String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                boolean isDateFormatted = DateUtil.isCellDateFormatted(cell);
                yield isDateFormatted ? cell.getDateCellValue().toString() : String.valueOf(cell.getNumericCellValue());
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }

    public static Date getCellDateValue(Cell cell) {
        if (cell == null) return null;
        return cell.getDateCellValue();
    }

}
