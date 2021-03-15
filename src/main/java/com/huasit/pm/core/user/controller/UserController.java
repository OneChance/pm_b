package com.huasit.pm.core.user.controller;


import com.huasit.pm.core.menu.entity.Menu;
import com.huasit.pm.core.menu.service.MenuService;
import com.huasit.pm.core.user.entity.User;
import com.huasit.pm.core.user.service.UserService;
import com.huasit.pm.system.locale.Response;
import com.huasit.pm.system.security.model.AuthenticationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    /**
     *
     */
    @ResponseBody
    @GetMapping("/self/")
    public ResponseEntity<Map<String, Object>> self(Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        List<Menu> menus = this.menuService.getUserMenuTree(loginUser.getSources().getId());
        return Response.success("user", loginUser.getSources(), "menus", menus).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/{id}/")
    public ResponseEntity<Map<String, Object>> get(@PathVariable Long id) {
        User user = this.userService.getUserById(id);
        return Response.success("user", user).entity();
    }

    /**
     *
     */
    @ResponseBody
    @GetMapping("/list/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> list(User form, @RequestParam("page") int page, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        Page<User> users = this.userService.list(form, page, pageSize);
        return Response.success("list", users.getContent(), "page", page, "count", users.getTotalElements(), "total_page", users.getTotalPages()).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PostMapping("/")
    @PreAuthorize("hasAuthority('sys_user_setting')")
    public ResponseEntity<Map<String, Object>> add(@RequestBody User form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.userService.add(form, loginUser.getSources());
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/{id}/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> update(@PathVariable Long id, @RequestBody User form) {
        this.userService.update(id, form);
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @DeleteMapping("/{id}/")
    @PreAuthorize("hasAnyRole('super','admin')")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        this.userService.delete(id);
        return Response.success("success", true).entity();
    }

    /**
     *
     */
    @ResponseBody
    @PutMapping("/password/")
    public ResponseEntity<Map<String, Object>> updatePassword(@RequestBody User form, Authentication authentication) {
        AuthenticationUser loginUser = (AuthenticationUser) authentication.getPrincipal();
        this.userService.updatePassword(form, loginUser.getSources());
        return Response.success("success", true).entity();
    }


    @Autowired
    UserService userService;

    @Autowired
    MenuService menuService;

}