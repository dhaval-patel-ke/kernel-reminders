package com.kernelequity.util;

import com.kernelequity.BuildConfig;
import com.kernelequity.GoogleChatClient;

public class GoogleChatUtil {

    public void notify(String title, String message) {
        new GoogleChatClient.Builder(BuildConfig.getGoogleChatWebhook())
                .title(title)
                .subtitle(message)
                .build()
                .sendMessage();
    }

    public void notify(String title, String message, String description) {
        new GoogleChatClient.Builder(BuildConfig.getGoogleChatWebhook())
                .title(title)
                .subtitle(message)
                .description(description)
                .build()
                .sendMessage();
    }

}
