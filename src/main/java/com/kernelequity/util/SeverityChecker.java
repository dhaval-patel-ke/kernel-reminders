package com.kernelequity.util;

import java.util.Arrays;
import java.util.Map;

public class SeverityChecker {

    private final Map<String, String> severityMap;

    public SeverityChecker(Map<String, String> severityMap) {
        this.severityMap = severityMap;
    }

    public boolean shouldNotify(String severity, long daysLeft) {
        if (daysLeft <= 0) return true;

        if (severity == null) {
            severity = "LOW";
        }

        String days = severityMap.get(severity);
        if (days == null || days.isBlank()) return false;

        String[] daysList = days.split(", ");
        return Arrays.stream(daysList).anyMatch(v -> v.contains(String.valueOf(daysLeft)));
    }

}
