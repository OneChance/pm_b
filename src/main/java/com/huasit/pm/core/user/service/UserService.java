package com.huasit.pm.core.user.service;

import com.huasit.pm.core.permission.entity.PermissionRepository;
import com.huasit.pm.core.role.entity.RoleRepository;
import com.huasit.pm.core.user.entity.User;
import com.huasit.pm.core.user.entity.UserRepository;
import com.huasit.pm.core.user.entity.UserToken;
import com.huasit.pm.core.user.entity.UserTokenRepository;
import com.huasit.pm.system.exception.SystemError;
import com.huasit.pm.system.exception.SystemException;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class UserService {

    /**
     *
     */
    public User getUserById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    /**
     *
     */
    public Page<User> list(User form, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Order.desc("id")));
        return this.userRepository.findAll((Specification<User>) (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (form.getState() != null) {
                predicates.add(cb.equal(root.get("state").as(User.UserState.class), form.getState()));
            }
            if (form.getUsername() != null && !form.getUsername().equals("")) {
                predicates.add(cb.like(root.get("username"), "%" + form.getUsername() + "%"));
            }
            if (form.getName() != null && !form.getName().equals("")) {
                predicates.add(cb.like(root.get("name"), "%" + form.getName() + "%"));
            }
            query.where(predicates.toArray(new Predicate[0]));
            return query.getRestriction();
        }, pageRequest);
    }

    /**
     *
     */
    public void add(User form, User loginUser) {
        form.setId(null);
        form.setCreateTime(new Date());
        form.setCreatorId(loginUser.getId());
        form.setState(User.UserState.NORMAL);
        User usernameCheck = this.userRepository.findByUsername(form.getUsername());
        if (usernameCheck != null) {
            throw new SystemException(SystemError.USERNAME_EXISTS);
        }
        if (StringUtil.isNullOrEmpty(form.getPassword())) {
            form.setPassword("");
        } else {
            form.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        }
        this.userRepository.save(form);
    }

    public void update(Long id, User form) {
        User db = this.userRepository.findById(id).orElseThrow(() -> new SystemException(SystemError.FORMDATA_ERROR));
        form.setId(id);
        User usernameCheck = this.userRepository.findByUsername(form.getUsername());
        if (usernameCheck != null && !usernameCheck.getId().equals(form.getId())) {
            throw new SystemException(SystemError.USERNAME_EXISTS);
        }
        if (!db.getPassword().equals(form.getPassword())) {
            form.setPassword(new BCryptPasswordEncoder().encode(form.getPassword()));
        }
        form.setCreatorId(db.getCreatorId());
        form.setCreateTime(db.getCreateTime());
        this.userRepository.save(form);
    }


    /**
     *
     */
    public void updatePassword(User form, User loginUser) {
        this.userRepository.updatePassword(loginUser.getId(), new BCryptPasswordEncoder().encode(form.getPassword()));
    }

    /**
     *
     */
    public void delete(Long id) {
        this.userRepository.updateState(User.UserState.DELETE, id);
    }

    /**
     *
     */
    public User loadLoginUserByToken(String token) {
        UserToken userToken = this.userTokenRepository.findByToken(token);
        if (userToken == null || userToken.getExpireDate().before(new Date()) || !userToken.getUser().isLogin()) {
            return null;
        }
        return userToken.getUser();
    }

    /**
     *
     */
    public User loadLoginUserByUsername(String username) {
        User user = this.userRepository.findByUsername(username);
        if (user == null || !user.isLogin()) {
            return null;
        }
        return user;
    }


    @Value("${authorization.expiration}")
    private long tokenExpiration;

    public UserToken createLoginUserToken(String username, String ip, String client) {
        Date now = new Date();
        User user = this.userRepository.findByUsername(username);
        UserToken userToken = new UserToken();
        userToken.setEnable(true);
        userToken.setUser(user);
        userToken.setToken(UUID.randomUUID().toString());
        userToken.setExpireDate(new Date(now.getTime() + tokenExpiration * 1000));
        userToken.setIp(ip);
        userToken.setClient(client);
        userToken.setCreateTime(now);
        this.userTokenRepository.save(userToken);
        return userToken;
    }


    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserTokenRepository userTokenRepository;

    @Autowired
    PermissionRepository permissionRepository;
}