package com.pasteleria.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "PRODUCTO")
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name = "nombre_producto")
    private String nombre;

    @Column(name = "descripcion_producto")
    private String descripcion;

    @Column(name = "precio_producto")
    private Double precio;

    @Column(name = "stock_producto")
    private Integer stock;

    @Column(name = "imagen_producto")
    private String imagenUrl;

    @ManyToOne
    @JoinColumn(name = "id_categoria", nullable = false)
    private Categoria categoria;
}
