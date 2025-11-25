package com.pasteleria.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaLogin {

    private String token;
    @Builder.Default
    private String type = "Bearer";
    private Long userId;
    private String nombre;
    private String email;
    private String rol;
    private String cupon;
    private LocalDate fechaNacimiento;
    private Long expiresIn;
}
