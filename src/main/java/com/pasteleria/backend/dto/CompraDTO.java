package com.pasteleria.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class CompraDTO {
    private Long usuarioId;
    private List<DetalleCompraDTO> items;
}
