package com.huasit.pm.core.menu.entity;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 *
 */
@Transactional
public interface MenuRepository extends CrudRepository<Menu, Long>, JpaSpecificationExecutor<Menu> {
    List<Menu> findAllByDelFalseOrderByOrderIndexAsc();
}