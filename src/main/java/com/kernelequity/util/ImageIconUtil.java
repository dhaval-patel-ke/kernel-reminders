package com.kernelequity.util;

import java.util.HashMap;
import java.util.Map;

public class ImageIconUtil {

    private final static Map<String, String> ICON_MAP = new HashMap<>();

    public static void setup() {
        ICON_MAP.put("Kernel", "https://dhaval-patel-ke.github.io/kernel-images/logo/kernel.png");
        ICON_MAP.put("Clearly", "https://dhaval-patel-ke.github.io/kernel-images/logo/clearly-legal.png");
        ICON_MAP.put("Engage", "https://dhaval-patel-ke.github.io/kernel-images/logo/engage-by-clarity.png");
        ICON_MAP.put("SimplyCare", "https://dhaval-patel-ke.github.io/kernel-images/logo/simplycare.png");
        ICON_MAP.put("Hopdoc", "https://dhaval-patel-ke.github.io/kernel-images/logo/hopdoc.png");
        ICON_MAP.put("Cimgpeds", "https://dhaval-patel-ke.github.io/kernel-images/logo/cimgpeds.png");
        ICON_MAP.put("Compass", "https://dhaval-patel-ke.github.io/kernel-images/logo/compass.png");
        ICON_MAP.put("Compass Inventory Scanner", "https://dhaval-patel-ke.github.io/kernel-images/logo/compass-inventory-scanner.png");
    }

    public static String getIconUrl(String name) {
        for (Map.Entry<String, String> entry : ICON_MAP.entrySet()) {
            if (name.contains(entry.getKey())) return entry.getValue();
        }
        return ICON_MAP.get("Kernel");
    }

    public static String getDefaultIconUrl() {
        return ICON_MAP.get("Kernel");
    }

}
