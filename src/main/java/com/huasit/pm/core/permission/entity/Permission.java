package com.huasit.pm.core.permission.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.huasit.pm.core.menu.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SYS_PERMISSION")
public class Permission implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @Column(nullable = false)
    private boolean del;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long creatorId;

    @Column(nullable = false)
    private Date createTime;

    @OneToOne
    Menu menu;

}
