package com.huasit.pm.core.role.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "SYS_ROLE")
public class Role implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @Column(nullable = false)
    private boolean del;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long creatorId;

    @Column(nullable = false)
    private Date createTime;
}
