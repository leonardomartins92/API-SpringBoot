package com.voluptaria.vlpt.service;

import com.voluptaria.vlpt.exception.SenhaInvalidaException;
import com.voluptaria.vlpt.model.Usuario;
import com.voluptaria.vlpt.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class UsuarioService implements UserDetailsService {

    private final PasswordEncoder encoder;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public UsuarioService(PasswordEncoder encoder, UsuarioRepository usuarioRepository) {
        this.encoder = encoder;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Usuario save(Usuario usuario){return usuarioRepository.save(usuario);}

    public UserDetails autenticar(Usuario usuario){
        UserDetails user = loadUserByUsername(usuario.getLogin());
        boolean senhaCorreta = encoder.matches(usuario.getSenha(), user.getPassword());
        if (senhaCorreta){
            return user;
        }
        throw new SenhaInvalidaException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByLogin(username)
                .orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado"));

        String[] roles = usuario.getAdmin() ? new String[] {"ADMIN", "USER"} : new String[]{"USER"};

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(roles)
                .build();
    }
}
