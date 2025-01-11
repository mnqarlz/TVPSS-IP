package com.tvpss.service;

import com.tvpss.dao.UserDao;
import com.tvpss.entity.User;
import com.tvpss.enums.Role;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service("userService")
public class UserService implements UserDetailsService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserDao userDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }

        String role = "ROLE_" + user.getRole().name();
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }

    public User findUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public User findUserById(Long id) {
        return userDao.findById(id);
    }

    public List<User> findAllUsers() {
        return userDao.findAll();
    }

    public List<User> findUsersByRole(Role role) {
    	return role != null ? userDao.findByRole(role) : userDao.findAll();
    }

    public void saveUser(User user) {
        user.setPassword(encodePassword(user.getPassword()));
        userDao.save(user);
    }

    public void updateUser(User user) {
        if (user.getId() != null) {
            user.setPassword(encodePassword(user.getPassword()));
            userDao.save(user);
        }
    }

    public void deleteUserById(Long id) {
        userDao.deleteById(id);
    }

    private String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public String determineDashboard(Role role) {
        switch (role) {
            case SUPER_ADMIN:
                return "/1-SuperAdmin/dashboardSA";
            case STATE_ADMIN:
                return "/2-StateAdmin/dashboardStA";
            case PPD_ADMIN:
                return "/3-PPDAdmin/dashboardPPD";
            case SCHOOL_ADMIN:
                return "/4-SchoolAdmin/dashboardScA";
            default:
                throw new IllegalArgumentException("Unknown role: " + role);
        }
    }
}
