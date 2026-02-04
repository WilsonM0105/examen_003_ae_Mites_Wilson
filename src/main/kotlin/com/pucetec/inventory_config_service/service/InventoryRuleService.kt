package com.pucetec.inventory_config_service.service

import com.pucetec.inventory_config_service.dto.InventoryRuleRequest
import com.pucetec.inventory_config_service.dto.InventoryRuleResponse
import com.pucetec.inventory_config_service.exception.BadRequestException
import com.pucetec.inventory_config_service.exception.NotFoundException
import com.pucetec.inventory_config_service.mapper.InventoryRuleMapper
import com.pucetec.inventory_config_service.repository.InventoryRuleRepository
import com.pucetec.inventory_config_service.security.JwtUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class InventoryRuleService(
    private val repo: InventoryRuleRepository
) {

    fun listAll(): List<InventoryRuleResponse> =
        repo.findAll().map(InventoryRuleMapper::toResponse)

    fun getById(id: Long): InventoryRuleResponse {
        val entity = repo.findById(id).orElseThrow { NotFoundException("InventoryRule id=$id no existe") }
        return InventoryRuleMapper.toResponse(entity)
    }

    fun searchByName(q: String): List<InventoryRuleResponse> {
        if (q.isBlank()) throw BadRequestException("q no puede estar vacío")
        return repo.findByNameContainingIgnoreCase(q.trim()).map(InventoryRuleMapper::toResponse)
    }

    @Transactional
    fun create(req: InventoryRuleRequest): InventoryRuleResponse {
        val userId = JwtUtils.userIdFromSub() // auditoría obligatoria
        val saved = repo.save(InventoryRuleMapper.toEntity(req, userId))
        return InventoryRuleMapper.toResponse(saved)
    }

    @Transactional
    fun update(id: Long, req: InventoryRuleRequest): InventoryRuleResponse {
        val userId = JwtUtils.userIdFromSub()
        val entity = repo.findById(id).orElseThrow { NotFoundException("InventoryRule id=$id no existe") }
        val updated = repo.save(InventoryRuleMapper.applyUpdate(entity, req, userId))
        return InventoryRuleMapper.toResponse(updated)
    }

    @Transactional
    fun toggle(id: Long): InventoryRuleResponse {
        val userId = JwtUtils.userIdFromSub()
        val entity = repo.findById(id).orElseThrow { NotFoundException("InventoryRule id=$id no existe") }
        entity.isActive = !entity.isActive
        entity.updatedBy = userId
        return InventoryRuleMapper.toResponse(repo.save(entity))
    }

    @Transactional
    fun delete(id: Long) {
        JwtUtils.userIdFromSub() // auditar intención (mínimo validar que hay userId)
        val entity = repo.findById(id).orElseThrow { NotFoundException("InventoryRule id=$id no existe") }
        repo.delete(entity)
    }
}
