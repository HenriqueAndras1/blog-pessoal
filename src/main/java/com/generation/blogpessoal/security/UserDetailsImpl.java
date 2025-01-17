package com.generation.blogpessoal.security;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.generation.blogpessoal.model.Usuario;

public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserDetailsImpl(Usuario user) {
        this.userName = user.getUsuario();
        this.password = user.getSenha();
    }

    public UserDetailsImpl() { }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() { 
        return true;
     // isAccountNonExpired(),Indica se o acesso do usuário expirou (tempo de acesso). Uma conta expirada não pode ser autenticada (return false).
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
     //isAccountNonLocked(), Indica se o usuário está bloqueado ou desbloqueado. Um usuário bloqueado não pode ser autenticado (return false).
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
     //isCredentialsNonExpired(), Indica se as credenciais do usuário (senha) expiraram (precisa ser trocada). Senha expirada impede a autenticação (return false).
    }

    @Override
    public boolean isEnabled() {
        return true;
    //isEnabled(), Indica se o usuário está habilitado ou desabilitado. Um usuário desabilitado não pode ser autenticado (return false).
    }

}
