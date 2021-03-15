package com.huasit.pm.core.menu.service;

import com.huasit.pm.core.menu.entity.Menu;
import com.huasit.pm.core.menu.entity.MenuRepository;
import com.huasit.pm.core.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MenuService {

    /**
     *
     */
    public Menu getMenuById(Long id) {
        return this.menuRepository.findById(id).orElse(null);
    }

    /**
     *
     */
    public Page<Menu> list(Menu form, int page, int pageSize, User loginUser) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("id")));
        return this.menuRepository.findAll((Specification<Menu>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("del").as(boolean.class), false));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public List<Menu> getUserMenuTree(Long userId) {
        List<Menu> menus = this.menuRepository.findAllByDelFalseOrderByOrderIndexAsc();
        if (menus == null) {
            return null;
        }
        Menu parent = this.getUserMenuTree(null, menus);
        return parent.getChildrens();
    }

    /**
     *
     */
    private Menu getUserMenuTree(Menu menu, List<Menu> menus) {
        if (menu == null) {
            menu = new Menu();
            menu.setId(0L);
        }
        for (Menu m : menus) {
            if (!menu.getId().equals(m.getPid())) {
                continue;
            }
            List<Menu> childrens = menu.getChildrens();
            if (childrens == null) {
                childrens = new ArrayList<>();
            }
            Menu children = this.getUserMenuTree(m, menus);
            childrens.add(children);
            menu.setChildrens(childrens);
        }
        return menu;
    }

    /**
     *
     */
    @Autowired
    MenuRepository menuRepository;

}
