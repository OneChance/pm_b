package com.huasit.pm.core.user.entity;

import com.huasit.pm.core.permission.entity.Permission;
import com.huasit.pm.core.role.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "SYS_USER")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User implements Serializable {

    public enum UserState {
        NORMAL, DELETE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private UserState state;

    @Column(nullable = false)
    private boolean login;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @Column(nullable = false)
    private String sex;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String mobile;

    @Column(nullable = false)
    private Long creatorId;

    @Column(nullable = false)
    private Date createTime;

    @Transient
    private List<Role> roles;

    @Transient
    private List<Permission> permissions;

}
