package com.rodaaron.security_service.services;

import com.rodaaron.security_service.controller.dto.AuthCreateUserRequest;
import com.rodaaron.security_service.controller.dto.AuthLoginRequest;
import com.rodaaron.security_service.controller.dto.AuthResponse;
import com.rodaaron.security_service.persistence.entities.RoleEntity;
import com.rodaaron.security_service.persistence.entities.RoleEnum;
import com.rodaaron.security_service.persistence.entities.UserEntity;
import com.rodaaron.security_service.persistence.repository.IRoleRepository;
import com.rodaaron.security_service.persistence.repository.IUserRepository;
import com.rodaaron.security_service.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService{

    @Autowired
    private IRoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    public AuthResponse login(AuthLoginRequest loginRequest){
        String username = loginRequest.username();
        String password = loginRequest.password();

        Authentication authentication = this.authenticate(username, password);

        // asigna auth devuelta en authenticate
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtils.createToken(authentication);

        AuthResponse authResponse = new AuthResponse(username, "The user was successfully logged.", accessToken, true);

        return authResponse;
    }

    public Authentication authenticate(String username, String password){
        // carga datos del usuario
        UserDetails userDetails = this.loadUserByUsername(username);

        if (userDetails == null){
            throw new BadCredentialsException("Invalid credentials");
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // busca si existe usuario
        UserEntity userEntity = userRepository.findUserEntityByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User does not exist"));

        List<SimpleGrantedAuthority>  authorityList = new ArrayList<>();
        // asigna autoridades segun el rol
        authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(userEntity.getRoleEntity().getRoleName().name())));

        return new User(userEntity.getUsername(), userEntity.getPassword(),userEntity.isEnabled(),userEntity.isAccountNoExpired(), userEntity.isCredentialNoExpired(), userEntity.isAccountNoLocked(), authorityList);
    }

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest){

        String username = authCreateUserRequest.username();
        String password = authCreateUserRequest.password();

        RoleEntity roleEntity = roleRepository.findRoleEntityByRoleName(authCreateUserRequest.roleRequest().roleName());

        if (roleEntity == null){
            throw new IllegalArgumentException("The specified role does not exist");
        }

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roleEntity(roleEntity)
                .accountNoExpired(true)
                .accountNoLocked(true)
                .credentialNoExpired(true)
                .isEnabled(true)
                .build();

        userRepository.save(userEntity);

        ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(roleEntity.getRoleName().name())));

        Authentication authentication = new UsernamePasswordAuthenticationToken(userEntity.getUsername(), userEntity.getPassword(), authorityList);

        String accessToken = jwtUtils.createToken(authentication);

        AuthResponse authResponse = new AuthResponse(userEntity.getUsername(), "The user was successfully created", accessToken, true);

        return authResponse;
    }

}
