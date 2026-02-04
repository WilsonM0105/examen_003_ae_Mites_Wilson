package com.pucetec.inventory_config_service.mapper

import com.pucetec.inventory_config_service.dto.InventoryRuleRequest
import com.pucetec.inventory_config_service.dto.InventoryRuleResponse
import com.pucetec.inventory_config_service.entity.InventoryRule

object InventoryRuleMapper {

    fun toEntity(req: InventoryRuleRequest, userId: String): InventoryRule =
        InventoryRule(
            name = req.name.trim(),
            description = req.description,
            isActive = req.isActive ?: true,
            updatedBy = userId
        )

    fun applyUpdate(entity: InventoryRule, req: InventoryRuleRequest, userId: String): InventoryRule {
        entity.name = req.name.trim()
        entity.description = req.description
        if (req.isActive != null) entity.isActive = req.isActive
        entity.updatedBy = userId
        return entity
    }

    fun toResponse(entity: InventoryRule): InventoryRuleResponse =
        InventoryRuleResponse(
            id = entity.id!!,
            name = entity.name,
            description = entity.description,
            isActive = entity.isActive,
            updatedBy = entity.updatedBy,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
}
