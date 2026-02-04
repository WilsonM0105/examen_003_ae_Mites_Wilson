package com.pucetec.inventory_config_service.dto

import java.time.Instant

data class InventoryRuleResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val isActive: Boolean,
    val updatedBy: String,
    val createdAt: Instant,
    val updatedAt: Instant
)
