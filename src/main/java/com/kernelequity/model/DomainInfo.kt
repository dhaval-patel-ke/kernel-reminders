package com.kernelequity.model

data class DomainInfo(
    val name: String,
    val domain: String,
    val severity: String,
    val title: String? = null,
    val note: String? = null,
)
