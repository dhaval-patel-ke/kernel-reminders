package com.kernelequity.task;

import com.kernelequity.model.DomainInfo;
import org.jetbrains.annotations.NotNull;

public interface DomainCertificateListener {

    public void onValidate(@NotNull  DomainInfo info, String title, String message);
}
