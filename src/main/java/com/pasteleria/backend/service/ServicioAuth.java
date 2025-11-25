package com.pasteleria.backend.service;

import com.pasteleria.backend.dto.auth.SolicitudLogin;
import com.pasteleria.backend.dto.auth.RespuestaLogin;
import com.pasteleria.backend.dto.auth.SolicitudRegistro;
import com.pasteleria.backend.model.Usuario;
import com.pasteleria.backend.repository.UsuarioRepository;
import com.pasteleria.backend.security.UtilJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ServicioAuth {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UtilJwt jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Value("${jwt.expiration:86400000}")
    private Long jwtExpiration;

    @Transactional
    public RespuestaLogin login(SolicitudLogin loginRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            if (!usuario.getCuentaActiva()) {
                throw new RuntimeException("Cuenta desactivada");
            }

            String token = jwtUtil.generateToken(usuario);

            return RespuestaLogin.builder()
                .token(token)
                .userId(usuario.getId())
                .nombre(usuario.getNombre())
                .email(usuario.getEmail())
                .rol(usuario.getRol())
                .expiresIn(jwtExpiration)
                .build();

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Email o contraseña incorrectos");
        }
    }

    @Transactional
    public RespuestaLogin register(SolicitudRegistro registerRequest) {
        if (usuarioRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        Usuario nuevoUsuario = Usuario.builder()
            .nombre(registerRequest.getNombre())
            .email(registerRequest.getEmail())
            .contrasena(passwordEncoder.encode(registerRequest.getPassword()))
            .fechaNacimiento(registerRequest.getFechaNacimiento())
            .rol(registerRequest.getRol().toUpperCase())
            .cuentaActiva(true)
            .fechaCreacion(LocalDateTime.now())
            .build();

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);

        String token = jwtUtil.generateToken(usuarioGuardado);

        return RespuestaLogin.builder()
            .token(token)
            .userId(usuarioGuardado.getId())
            .nombre(usuarioGuardado.getNombre())
            .email(usuarioGuardado.getEmail())
            .rol(usuarioGuardado.getRol())
            .expiresIn(jwtExpiration)
            .build();
    }
}
