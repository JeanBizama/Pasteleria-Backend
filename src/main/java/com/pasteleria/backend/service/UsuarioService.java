package com.pasteleria.backend.service;

import com.pasteleria.backend.model.Usuario;
import com.pasteleria.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email).orElse(null);
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario actualizarPerfil(String emailUsuario, Usuario datosActualizados) {

        // 1. Buscar el usuario existente por el email
        Optional<Usuario> optionalUsuario = usuarioRepository.findByEmail(emailUsuario);
        if (optionalUsuario.isEmpty()) {
            // Lanza una excepción si el usuario no existe (aunque no debería pasar con un JWT válido)
            throw new RuntimeException("Usuario con email " + emailUsuario + " no encontrado.");
        }
        Usuario usuarioExistente = optionalUsuario.get();

        // Nombre
        if (datosActualizados.getNombre() != null && !datosActualizados.getNombre().isBlank()) {
            usuarioExistente.setNombre(datosActualizados.getNombre());
        }
        // Fecha de Nacimiento

        usuarioExistente.setCupon(datosActualizados.getCupon());

        return usuarioRepository.save(usuarioExistente);
    }
}
