package com.kernelequity;

import com.kernelequity.util.GoogleSheetUtil;
import com.kernelequity.util.ImageIconUtil;
import com.kernelequity.util.SeverityChecker;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.LinkedHashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        ImageIconUtil.setup();

        String downloadLink = BuildConfig.getGoogleSheetDownloadLink();
        new GoogleSheetUtil().download(downloadLink, BuildConfig.DATA_PATH);

        Map<String, String> severityMap = readSeverityMap();
        SeverityChecker checker = new SeverityChecker(severityMap);
      //  new GeneralReminders(checker).verify();
        System.out.println("\n\n\n");
        new DomainReminders(checker).verify();
        System.out.println("\n\n\n");
        new WebPortalValidator().verify();
    }

    private static Map<String, String> readSeverityMap() {
        Map<String, String> data = new LinkedHashMap<>();
        Sheet sheet = GoogleSheetUtil.readSheet(0);
        if (sheet == null) return data;
        for (Row row : sheet) {
            String key = GoogleSheetUtil.getCellValue(row.getCell(0));
            String value = GoogleSheetUtil.getCellValue(row.getCell(1));
            data.put(key, value);
        }
        return data;
    }
}
