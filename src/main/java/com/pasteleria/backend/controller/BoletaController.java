package com.pasteleria.backend.controller;

import com.pasteleria.backend.dto.CompraDTO;
import com.pasteleria.backend.model.Boleta;
import com.pasteleria.backend.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/boletas")
@Tag(name = "Ventas", description = "Operaciones de compra y facturación")
@CrossOrigin(origins = "*")
public class BoletaController {

    @Autowired
    private BoletaService boletaService;

    @PostMapping("/comprar")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Generar una nueva venta",
            description = "Recibe un ID de usuario y una lista de productos con cantidades. Descuenta stock automáticamente. Requiere autenticación.")
    public ResponseEntity<?> realizarCompra(@RequestBody CompraDTO CompraDTO) {
        try {
            Boleta nuevaBoleta = boletaService.generarVenta(CompraDTO);
            return ResponseEntity.ok(nuevaBoleta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @Operation(summary = "Listar todas las boletas", description = "Solo ADMIN y VENDEDOR pueden ver todas las ventas")
    public ResponseEntity<List<Boleta>> listarBoletas() {
        List<Boleta> boletas = boletaService.listarBoletas();
        return ResponseEntity.ok(boletas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @Operation(summary = "Obtener boleta por ID", description = "Solo ADMIN y VENDEDOR")
    public ResponseEntity<Boleta> obtenerBoleta(@PathVariable Long id) {
        Boleta boleta = boletaService.buscarBoletaPorId(id);
        return ResponseEntity.ok(boleta);
    }

}
