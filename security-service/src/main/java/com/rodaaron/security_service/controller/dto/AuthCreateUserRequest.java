package com.rodaaron.security_service.controller.dto;

import com.rodaaron.security_service.persistence.entities.RoleEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record AuthCreateUserRequest(@NotBlank String username,@NotBlank String password,@Valid AuthCreateRoleRequest roleRequest) {
}
