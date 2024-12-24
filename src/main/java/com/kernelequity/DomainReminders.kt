package com.kernelequity

import com.kernelequity.design.*
import com.kernelequity.model.DomainInfo
import com.kernelequity.task.DomainCertificateCheckTask
import com.kernelequity.task.DomainCertificateListener
import com.kernelequity.util.GoogleSheetUtil
import com.kernelequity.util.ImageIconUtil
import com.kernelequity.util.SeverityChecker
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor

class DomainReminders(private val checker: SeverityChecker) : DomainCertificateListener {

    private val list = mutableListOf<Widget>()

    fun verify() {
        val sheet = GoogleSheetUtil.readSheet(2) ?: return

        var index = 0
        System.out.printf(
            "%-32s %-32s %-16s %-10s %-10s %-10s\n",
            "Name",
            "Domain",
            "Date",
            "Severity",
            "Days-Left",
            "Notify"
        )
        println("-----------------------------------------------------------------------------------------------------------------")

        // Create a ThreadPoolExecutor with 2 core threads, a max of 4 threads, and a keep-alive time of 10 seconds
        val executor = Executors.newFixedThreadPool(5) as ThreadPoolExecutor
        for (row in sheet) {
            index++
            if (index == 1) continue

            val name = GoogleSheetUtil.getCellValue(row.getCell(0))
            val domain = GoogleSheetUtil.getCellValue(row.getCell(1))
            val title = GoogleSheetUtil.getCellValue(row.getCell(2))
            val severity = GoogleSheetUtil.getCellValue(row.getCell(3))
            val checks = GoogleSheetUtil.getCellValue(row.getCell(4))
            val note = GoogleSheetUtil.getCellValue(row.getCell(5))

            if (domain.isBlank()) continue
            if (title.isNotBlank()) continue
            if (!checks.contains("SSL Check")) continue

            val domainInfo = DomainInfo(
                name = name,
                domain = domain,
                severity = severity,
                title = title,
                note = note,
            )

            val task = DomainCertificateCheckTask(domainInfo, checker, this)
            executor.execute(task)
        }

        // Shut down the executor after tasks are completed
        executor.shutdown()

        while (!executor.isTerminated) {
        }

        if (list.isNotEmpty()) {
            GoogleChatClientV2.Builder(BuildConfig.getGoogleChatWebhook())
                .imageUrl(ImageIconUtil.getDefaultIconUrl())
                .title("Kernel Reminders")
                .subtitle("Domain Availability and SSL Certificate Health Check")
                .widgets(list)
                .build()
                .sendMessage()
        }
    }

    override fun onValidate(info: DomainInfo, title: String?, message: String?) {
        println(info.name + "=>" + title + "=>" + message)
        if (list.isNotEmpty()) {
            list.add(Divider())
        }

        list.add(
            RichTextLayout(
                text = RichText(
                    text = title ?: "",
                    bottomLabel = message ?: "",
                    icon = IconUrl(
                        url = ImageIconUtil.getIconUrl(info.name)
                    )
                )
            )
        )
    }
}