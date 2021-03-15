package com.huasit.pm.core.role.controller;

import com.huasit.pm.core.role.entity.Role;
import com.huasit.pm.core.role.service.RoleService;
import com.huasit.pm.system.locale.Response;
import com.huasit.pm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/role")
public class RoleController {

    @ResponseBody
    @GetMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        Role role = this.roleService.getRoleById(id);
        return Response.success("role", role).entity();
    }

    @ResponseBody
    @GetMapping("/list/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> list(Role form, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        Page<Role> users = this.roleService.list(form, page, pageSize);
        return Response.success("list", users.getContent(), "page", page, "count", users.getTotalElements(), "total_page", users.getTotalPages()).entity();
    }

    @ResponseBody
    @PostMapping("/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> add(@RequestBody @Valid Role form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.roleService.add(form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    @ResponseBody
    @PutMapping("/{id}/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody @Valid Role form) {
        this.roleService.update(id, form);
        return Response.success("success", true).entity();
    }

    @ResponseBody
    @DeleteMapping("/{id}/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        this.roleService.delete(id);
        return Response.success("success", true).entity();
    }


    @Autowired
    RoleService roleService;
}