package com.pucetec.inventory_config_service.repository

import com.pucetec.inventory_config_service.entity.InventoryRule
import org.springframework.data.jpa.repository.JpaRepository


interface InventoryRuleRepository : JpaRepository<InventoryRule, Long> {
    fun findByIsActiveTrue(): List<InventoryRule>
    fun findByNameContainingIgnoreCase(name: String): List<InventoryRule>
}
