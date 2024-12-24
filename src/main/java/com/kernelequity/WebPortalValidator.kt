package com.kernelequity

import com.kernelequity.design.*
import com.kernelequity.util.GoogleSheetUtil
import com.kernelequity.util.ImageIconUtil
import com.microsoft.playwright.Playwright

class WebPortalValidator {
    private val list = mutableListOf<Widget>()

    fun verify() {
        val sheet = GoogleSheetUtil.readSheet(2) ?: return

        var index = 0
        System.out.printf("%-32s %-32s %-16s\n", "Name", "Web Portal", "Title")
        println("-----------------------------------------------------------------------------------------------------------------")

        val playwright = Playwright.create()
        val browser = playwright.chromium().launch()

        // Create a ThreadPoolExecutor with 2 core threads, a max of 4 threads, and a keep-alive time of 10 seconds
        for (row in sheet) {
            index++
            if (index == 1) continue

            val name = GoogleSheetUtil.getCellValue(row.getCell(0))
            val portal = GoogleSheetUtil.getCellValue(row.getCell(1))
            val title = GoogleSheetUtil.getCellValue(row.getCell(2))
            val checks = GoogleSheetUtil.getCellValue(row.getCell(4))
            if (name.isBlank() || title.isBlank() || !checks.contains("Playwright")) continue

            System.out.printf("%-32s %-32s %-16s\n", name, portal, title)

            val page = browser.newPage()
            page.navigate("https://$portal")

            if (title != page.title()) {
                if (list.isNotEmpty()) {
                    list.add(Divider())
                }

                list.add(
                    RichTextLayout(
                        text = RichText(
                            text = name,
                            bottomLabel = "https://$portal is not reachable \uD83D\uDEA8\uD83D\uDEA8.",
                            icon = IconUrl(
                                url = ImageIconUtil.getIconUrl(name)
                            )
                        )
                    )
                )
            }
        }

        playwright.close()

        if (list.isNotEmpty()) {
            GoogleChatClientV2.Builder(BuildConfig.getGoogleChatWebhook())
                .imageUrl(ImageIconUtil.getDefaultIconUrl())
                .title("Kernel Reminders")
                .subtitle("Domain Availability Check")
                .widgets(list)
                .build()
                .sendMessage()
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            WebPortalValidator().verify()
        }
    }
}


