package com.kernelequity.task;

import com.kernelequity.model.DomainInfo;
import com.kernelequity.util.DateUtil;
import com.kernelequity.util.DomainChecker;
import com.kernelequity.util.SeverityChecker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DomainCertificateCheckTask implements Runnable {
    private final SeverityChecker checker;
    private final DomainInfo info;
    private final DomainCertificateListener listener;

    public DomainCertificateCheckTask(DomainInfo info, SeverityChecker checker, DomainCertificateListener listener) {
        this.info = info;
        this.checker = checker;
        this.listener = listener;
    }

    @Override
    public void run() {
        String tmpDomain = info.getDomain().replaceFirst("^(https?://)", "");
        if (!DomainChecker.isDomainUp(tmpDomain)) {
            notify(tmpDomain, null, -1);
            return;
        }

        // Extract expiration date
        Date expirationDate = DomainChecker.getCertificateExpirationDate(tmpDomain);
        long daysLeft = DateUtil.getDifferenceDays(new Date(), expirationDate);
        boolean shouldNotify = checker.shouldNotify(info.getSeverity(), daysLeft);
        String formattedDate = DateUtil.getFormattedDate(expirationDate);

        System.out.printf("%-32s %-32s %-16s %-10s %-10s %-10s\n", info.getName(), tmpDomain, formattedDate, info.getSeverity(), daysLeft, shouldNotify);
        if (shouldNotify) {
            notify(tmpDomain, expirationDate, daysLeft);
        }
    }

    public void notify(String domain, Date expirationDate, long daysLeft) {
        String title = info.getName();
        String message;
        String description;
        if (daysLeft < 0) {
            message = " is not reachable \uD83D\uDEA8\uD83D\uDEA8";
        } else if (expirationDate == null) {
            message = " SSL certificate is either expired or has not yet been properly configured. \uD83D\uDEA8\uD83D\uDEA8";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");
            String formattedExpirationDate = sdf.format(expirationDate);

            message = " SSL certificate is about to expire on " + formattedExpirationDate + " (" + daysLeft + " days left) ⌛";
            if (daysLeft == 1) {
                message = " SSL certificate is about to expire on " + formattedExpirationDate + " (" + daysLeft + " day left) ⌛";
            } else if (daysLeft <= 0) {
                message = " SSL certificate expired on " + formattedExpirationDate + " \uD83D\uDEA8\uD83D\uDEA8";
            }
        }

        description = "<a href=\"https://" + domain + "\">" + domain + "</a>" + message;
        listener.onValidate(info, title, description);
    }
}
