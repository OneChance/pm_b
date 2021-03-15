package com.huasit.pm.core.permission.service;

import com.huasit.pm.core.permission.entity.Permission;
import com.huasit.pm.core.permission.entity.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PermissionService {

    public List<Permission> getValidPermissions() {
        return permissionRepository.findAllByDelFalse();
    }

    @Autowired
    PermissionRepository permissionRepository;
}
