package com.pasteleria.backend.controller;

import com.pasteleria.backend.dto.auth.*;
import com.pasteleria.backend.service.ServicioAuth;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticación", description = "Endpoints para autenticación y gestión de tokens JWT")
@CrossOrigin(origins = "*")
public class ControladorAuth {

    @Autowired
    private ServicioAuth authService;

    @PostMapping("/login")
    @Operation(
        summary = "Iniciar sesión",
        description = "Autentica un usuario con email y contraseña, devuelve token JWT y refresh token"
    )
    public ResponseEntity<?> login(@Valid @RequestBody SolicitudLogin loginRequest) {
        try {
            RespuestaLogin response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            RespuestaError error = new RespuestaError(
                HttpStatus.UNAUTHORIZED.value(),
                e.getMessage(),
                "No autorizado"
            );
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    @PostMapping("/register")
    @Operation(
        summary = "Registrar nuevo usuario",
        description = "Crea un nuevo usuario en el sistema. Por defecto se crea con rol CLIENTE"
    )
    public ResponseEntity<?> register(@Valid @RequestBody SolicitudRegistro registerRequest) {
        try {
            RespuestaLogin response = authService.register(registerRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            RespuestaError error = new RespuestaError(
                HttpStatus.BAD_REQUEST.value(),
                e.getMessage(),
                "Solicitud inválida"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
}
