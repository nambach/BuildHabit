package io.nambm.buildhabit.service.impl;

import io.nambm.buildhabit.business.UserBusiness;
import io.nambm.buildhabit.model.user.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Qualifier("impl")
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserBusiness userBusiness;

    @Autowired
    public UserDetailsServiceImpl(UserBusiness userBusiness) {
        this.userBusiness = userBusiness;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel = userBusiness.get(username);
        if (userModel == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userModel.getRole()));

        return new User(userModel.getUsername(), userModel.getPassword(), authorities);
    }
}
