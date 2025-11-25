package com.pasteleria.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Long expiresIn;
}
