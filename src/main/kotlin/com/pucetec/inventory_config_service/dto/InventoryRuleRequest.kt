package com.pucetec.inventory_config_service.dto

import jakarta.validation.constraints.NotBlank

data class InventoryRuleRequest(
    @field:NotBlank(message = "name es requerido")
    val name: String,
    val description: String? = null,
    val isActive: Boolean? = null
)
