package com.pasteleria.backend.service;

import com.pasteleria.backend.dto.ProductoDTO;
import com.pasteleria.backend.model.Categoria;
import com.pasteleria.backend.model.Producto;
import com.pasteleria.backend.repository.CategoriaRepository;
import com.pasteleria.backend.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria obtenerOCrearCategoria(String nombreCategoria) {
        // 1. Buscar la categoría por nombre
        Optional<Categoria> categoriaOptional = categoriaRepository.findByNombre(nombreCategoria);

        if (categoriaOptional.isPresent()) {
            return categoriaOptional.get();
        } else {
            // 2. Si no existe, crear una nueva
            Categoria nuevaCategoria = new Categoria();
            nuevaCategoria.setNombre(nombreCategoria);

            // 3. Guardar y devolver la nueva categoría
            return categoriaRepository.save(nuevaCategoria);
        }
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto buscarProductoPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    public List<Producto> listarPorCategoria(Long idCategoria) {
        return productoRepository.findByCategoria_Id(idCategoria);
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public Producto guardarProducto(Producto producto, String nombreCategoria) {
        Categoria categoria = obtenerOCrearCategoria(nombreCategoria);
        producto.setCategoria(categoria);

        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Long id, ProductoDTO productoDTO, String nombreCategoria) {
        Producto productoExistente = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        // 1. Actualizar los campos
        productoExistente.setNombre(productoDTO.getNombre());
        productoExistente.setDescripcion(productoDTO.getDescripcion());
        productoExistente.setPrecio(productoDTO.getPrecio());
        productoExistente.setImagenUrl(productoDTO.getImagenUrl());
        // Asume que stock se queda como está si no se envía, o actualízalo si es necesario.

        // 2. Manejar la Categoría si se envió un nombre de categoría
        if (nombreCategoria != null && !nombreCategoria.trim().isEmpty()) {
            Categoria nuevaCategoria = obtenerOCrearCategoria(nombreCategoria);
            productoExistente.setCategoria(nuevaCategoria);
        }

        // 3. Guardar y devolver
        return productoRepository.save(productoExistente);
    }

    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
}
