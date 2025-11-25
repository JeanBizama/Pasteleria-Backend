package com.pasteleria.backend.controller;

import com.pasteleria.backend.model.Usuario;
import com.pasteleria.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios y clientes - Solo ADMIN")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/profile")
    @Operation(
            summary = "Obtener mi perfil",
            description = "Devuelve los datos del usuario autenticado basándose en el token JWT."
    )
    @PreAuthorize("isAuthenticated()") // Solo requiere un token válido
    public ResponseEntity<Usuario> obtenerPerfil(Authentication authentication) {

        String emailUsuario = authentication.getName();

        Usuario usuario = usuarioService.buscarPorEmail(emailUsuario);

        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(usuario);
    }


    @PutMapping("/profile")
    @Operation(
            summary = "Actualizar mi perfil",
            description = "Permite a cualquier usuario autenticado actualizar sus propios datos (nombre, fechaNacimiento, cupon, etc.). La contraseña debe manejarse por separado."
    )
    @PreAuthorize("isAuthenticated()") // Requiere que el usuario esté logueado (tenga un token válido)
    public ResponseEntity<Usuario> actualizarPerfil(
            @RequestBody Usuario datosActualizados,
            Authentication authentication) {

        String emailUsuario = authentication.getName();
        Usuario usuarioActualizado = usuarioService.actualizarPerfil(emailUsuario, datosActualizados);

        return ResponseEntity.ok(usuarioActualizado);
    }



    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Solo ADMIN - Devuelve un listado completo de usuarios registrados")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Solo ADMIN - Guarda un usuario en la base de datos")
    @PreAuthorize("hasRole('ADMIN')")
    public Usuario guardarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Solo ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public Usuario obtenerUsuario(@PathVariable Long id) {
        return usuarioService.buscarUsuarioPorId(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Solo ADMIN")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }

}
