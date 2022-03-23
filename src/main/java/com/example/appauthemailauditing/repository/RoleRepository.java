package com.example.appauthemailauditing.repository;

import com.example.appauthemailauditing.entity.Role;
import com.example.appauthemailauditing.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName roleName);
}
