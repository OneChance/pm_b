package com.huasit.pm.core.permission.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 */
@Transactional
public interface PermissionRepository extends CrudRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    List<Permission> findAllByDelFalse();
}