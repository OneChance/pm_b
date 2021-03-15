package com.huasit.pm.core.role.service;

import com.huasit.pm.core.role.entity.*;
import com.huasit.pm.core.user.entity.User;
import com.huasit.pm.system.exception.SystemError;
import com.huasit.pm.system.exception.SystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RoleService {

    public Role getRoleById(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    public Page<Role> list(Role form, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.asc("id")));
        return this.roleRepository.findAll((Specification<Role>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("del").as(boolean.class), false));
            predicates.add(cb.like(root.get("name"), "%" + form.getName() + "%"));
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    public void add(Role form, User loginUser) {
        form.setCreateTime(new Date());
        form.setCreatorId(loginUser.getId());
        form.setId(null);
        form.setDel(false);
        this.roleRepository.save(form);
    }

    public void update(Long id, Role form) {
        Role db = this.roleRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        form.setId(id);
        form.setCreatorId(db.getCreatorId());
        form.setCreateTime(db.getCreateTime());
        this.roleRepository.save(form);
    }

    public void delete(Long id) {
        this.roleRepository.updateDel(true, id);
    }


    @Autowired
    RoleRepository roleRepository;


}
