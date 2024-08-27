package com.rodaaron.security_service.controller.dto;

import com.rodaaron.security_service.persistence.entities.RoleEntity;
import com.rodaaron.security_service.persistence.entities.RoleEnum;
import org.springframework.validation.annotation.Validated;

@Validated
public record AuthCreateRoleRequest(RoleEnum roleName) {
}
