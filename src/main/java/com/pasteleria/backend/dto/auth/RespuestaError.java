package com.pasteleria.backend.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RespuestaError {

    private int status;
    private String mensaje;
    private String error;
    private long timestamp;

    public RespuestaError(int status, String mensaje, String error) {
        this.status = status;
        this.mensaje = mensaje;
        this.error = error;
        this.timestamp = System.currentTimeMillis();
    }
}
