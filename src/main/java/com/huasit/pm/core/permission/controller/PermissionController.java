package com.huasit.pm.core.permission.controller;

import com.huasit.pm.core.permission.service.PermissionService;
import com.huasit.pm.system.locale.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/permission")
public class PermissionController {
    @ResponseBody
    @GetMapping("/list/")
    public ResponseEntity<Map<String, Object>> list() {
        return Response.success("list", permissionService.getValidPermissions()).entity();
    }

    @Autowired
    PermissionService permissionService;
}
