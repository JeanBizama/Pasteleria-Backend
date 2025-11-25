package com.pasteleria.backend.controller;

import com.pasteleria.backend.model.Usuario;
import com.pasteleria.backend.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@Tag(name = "Usuarios", description = "Gestión de usuarios y clientes - Solo ADMIN")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    @Operation(summary = "Listar todos los usuarios", description = "Solo ADMIN - Devuelve un listado completo de usuarios registrados")
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo usuario", description = "Solo ADMIN - Guarda un usuario en la base de datos")
    public Usuario guardarUsuario(@RequestBody Usuario usuario) {
        return usuarioService.guardarUsuario(usuario);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Solo ADMIN")
    public Usuario obtenerUsuario(@PathVariable Long id) {
        return usuarioService.buscarUsuarioPorId(id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar usuario", description = "Solo ADMIN")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
    }

}
