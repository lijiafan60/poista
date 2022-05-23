package com.ljf.ploughthewaves.domain.admin.service;

import com.ljf.ploughthewaves.domain.admin.repository.IUserRepository;
import com.ljf.ploughthewaves.infrastructure.po.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if(user == null) {
            return null;
        } else {
            return new org.springframework.
                            security.core.
                            userdetails.User(username,
                                             passwordEncoder.encode(user.getPassword()),
                                             GetAuthorities(user.getRole())
                                            );
        }
    }
    private Collection<GrantedAuthority> GetAuthorities(String role) {
        List<String> roleLists = Arrays.asList(role.split(","));
        List<GrantedAuthority> arrayList = new ArrayList<>();
        for(String roleItem:roleLists) {
            arrayList.add(new SimpleGrantedAuthority(roleItem));
        }
        return arrayList;
    }
}
