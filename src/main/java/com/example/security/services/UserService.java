package com.example.security.services;

import com.example.security.dto.UserDto;
import com.example.security.model.UserEntity;
import com.example.security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userEntityDb = repository.findByUsername(username);

        if(userEntityDb == null){
            throw new UsernameNotFoundException("Usuaŕio não existe no banco de dados");
        }

        List<GrantedAuthority> authAdmin = AuthorityUtils.createAuthorityList("ADMIN", "USER");
        List<GrantedAuthority> authUser = AuthorityUtils.createAuthorityList("USER");

        return new User(userEntityDb.getUsername(), userEntityDb.getPassword(), userEntityDb.getIsAdmin() ? authAdmin : authUser);
    }

    public UserEntity createUser(UserDto userDto) {

        UserEntity username = repository.findByUsername(userDto.getUsername());

        if (Objects.nonNull(username)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists.");
        }

        UserEntity user = UserEntity.builder()
                .username(userDto.getUsername())
                .password(bCryptPasswordEncoder.encode(userDto.getPassword()))
                .isAdmin(userDto.getIsAdmin())
                .build();

        return repository.save(user);
    }
}
