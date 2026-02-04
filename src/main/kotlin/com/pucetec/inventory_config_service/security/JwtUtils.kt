package com.pucetec.inventory_config_service.security

import com.pucetec.inventory_config_service.exception.UnauthorizedActionException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

object JwtUtils {

    private fun jwtAuth(): JwtAuthenticationToken {
        val auth = SecurityContextHolder.getContext().authentication
        return auth as? JwtAuthenticationToken
            ?: throw UnauthorizedActionException("No existe JWT en el contexto de seguridad")
    }

    fun userIdFromSub(): String {
        val jwt = jwtAuth().token
        return jwt.getClaimAsString(JwtClaims.SUB)
            ?: throw UnauthorizedActionException("Falta claim '${JwtClaims.SUB}' (sub) en el JWT")
    }
}
