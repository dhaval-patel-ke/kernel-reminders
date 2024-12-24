package com.kernelequity;

public class BuildConfig {

    public static final String DATA_PATH = "project-reminders.xlsx";

    public static String getGoogleSheetDownloadLink() {
        return "https://docs.google.com/spreadsheets/d/" + getGoogleSheetId() + "/export?format=xlsx";
    }

    public static String getGoogleSheetLink() {
        return "https://docs.google.com/spreadsheets/d/" + getGoogleSheetId() + "/edit?usp=sharing";
    }

    public static String getGoogleSheetId() {
        String value = System.getenv("GOOGLE_SHEET_ID");
        if (value != null) {
            return value;
        }
        return "";
    }

    public static String getGoogleChatWebhook() {
        String value = System.getenv("GOOGLE_CHAT_WEBHOOK");
        if (value != null) {
            return value;
        }
        return "";
    }
}