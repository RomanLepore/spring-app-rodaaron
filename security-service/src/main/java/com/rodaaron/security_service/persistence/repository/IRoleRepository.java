package com.rodaaron.security_service.persistence.repository;

import com.rodaaron.security_service.persistence.entities.RoleEntity;
import com.rodaaron.security_service.persistence.entities.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findRoleEntityByRoleName(RoleEnum roleName);
}
