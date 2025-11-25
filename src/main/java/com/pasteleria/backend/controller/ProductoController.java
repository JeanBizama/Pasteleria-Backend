package com.pasteleria.backend.controller;

import com.pasteleria.backend.model.Producto;
import com.pasteleria.backend.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.pasteleria.backend.dto.ProductoDTO;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "Catálogo de pasteles y productos")
@CrossOrigin(origins = "*")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @GetMapping
    @Operation(summary = "Ver catálogo completo")
    public List<Producto> listarProductos() {
        return productoService.listarProductos();
    }

    @GetMapping("/categoria/{idCategoria}")
    @Operation(summary = "Filtrar productos por categoría", description = "Muestra los productos de una categoria especifica")
    public List<Producto> listarPorCategoria(@PathVariable Long idCategoria) {
        return productoService.listarPorCategoria(idCategoria);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @Operation(summary = "Agregar nuevo producto al inventario", description = "Solo ADMIN y VENDEDOR")
    public Producto guardarProducto(@RequestBody ProductoDTO productoDTO) {
        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setDescripcion(productoDTO.getDescripcion());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setImagenUrl(productoDTO.getImagenUrl());

        return productoService.guardarProducto(producto, productoDTO.getNombreCategoria());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR')")
    @Operation(summary = "Actualizar un producto existente", description = "Solo ADMIN y VENDEDOR")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        return productoService.actualizarProducto(id, productoDTO, productoDTO.getNombreCategoria());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Eliminar un producto", description = "Solo ADMIN")
    public void eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
    }
}
