package com.pasteleria.backend.service;

import com.pasteleria.backend.dto.DetalleCompraDTO;
import com.pasteleria.backend.dto.CompraDTO;
import com.pasteleria.backend.model.Boleta;
import com.pasteleria.backend.model.DetalleBoleta;
import com.pasteleria.backend.model.Producto;
import com.pasteleria.backend.model.Usuario;
import com.pasteleria.backend.repository.BoletaRepository;
import com.pasteleria.backend.repository.DetalleBoletaRepository;
import com.pasteleria.backend.repository.ProductoRepository;
import com.pasteleria.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class BoletaService {
    @Autowired
    private BoletaRepository boletaRepository;
    @Autowired
    private DetalleBoletaRepository detalleBoletaRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Boleta> listarBoletas() {
        return boletaRepository.findAll();
    }

    public Boleta buscarBoletaPorId(Long id) {
        return boletaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Boleta no encontrada con ID: " + id));
    }

    @Transactional
    public Boleta generarVenta(CompraDTO solicitud) {

        Usuario usuario = usuarioRepository.findById(solicitud.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Boleta boleta = new Boleta();
        boleta.setUsuario(usuario);
        boleta.setFecha(new Date());
        boleta.setTotal(0);

        boleta = boletaRepository.save(boleta);

        int totalCalculado = 0;

        for (DetalleCompraDTO item : solicitud.getItems()) {
            Producto producto = productoRepository.findById(item.getProductoId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado ID: " + item.getProductoId()));

            if (producto.getStock() < item.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
            }

            producto.setStock(producto.getStock() - item.getCantidad());
            productoRepository.save(producto);

            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setBoleta(boleta);
            detalle.setProducto(producto);
            detalle.setCantidad(item.getCantidad());

            double subtotal = producto.getPrecio() * item.getCantidad();
            detalle.setSubtotal(subtotal);

            detalleBoletaRepository.save(detalle);

            totalCalculado += (int) subtotal;
        }

        boleta.setTotal(totalCalculado);
        return boletaRepository.save(boleta);
    }
}
