package com.pucetec.inventory_config_service.controller

import com.pucetec.inventory_config_service.dto.InventoryRuleRequest
import com.pucetec.inventory_config_service.dto.InventoryRuleResponse
import com.pucetec.inventory_config_service.service.InventoryRuleService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/rules")
class InventoryRuleController(
    private val service: InventoryRuleService
) {

    // 2) Autenticado (GET)
    @GetMapping
    fun list(): List<InventoryRuleResponse> = service.listAll()

    @GetMapping("/{id}")
    fun get(@PathVariable id: Long): InventoryRuleResponse = service.getById(id)

    @GetMapping("/search")
    fun search(@RequestParam q: String): List<InventoryRuleResponse> = service.searchByName(q)

    // 3) Solo ADMIN (POST/PUT/PATCH/DELETE) por SecurityConfig
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody req: InventoryRuleRequest): InventoryRuleResponse =
        service.create(req)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @Valid @RequestBody req: InventoryRuleRequest): InventoryRuleResponse =
        service.update(id, req)

    @PatchMapping("/{id}/toggle")
    fun toggle(@PathVariable id: Long): InventoryRuleResponse =
        service.toggle(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) =
        service.delete(id)
}
