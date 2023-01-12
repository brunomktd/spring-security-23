package com.example.security.services;

import com.example.security.model.UserEntity;
import com.example.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntityDb = repository.findByUsername(username);

        if(userEntityDb == null){
            throw new UsernameNotFoundException("Usuaŕio não existe no banco de dados");
        }

        List<GrantedAuthority> authAdmin = AuthorityUtils.createAuthorityList("ADMIN", "USER");
        List<GrantedAuthority> authUser = AuthorityUtils.createAuthorityList("USER");

        return new User(userEntityDb.getUsername(), userEntityDb.getPassword(), userEntityDb.isAdmin() ? authAdmin : authUser);
    }
}
