package com.pucetec.inventory_config_service.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/public")
class PublicController {

    @GetMapping("/health")
    fun health() = mapOf(
        "status" to "OK",
        "timestamp" to Instant.now().toString()
    )
}
