package com.pasteleria.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "BOLETA")
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_boleta")
    private long id;

    @Column(name = "fecha_boleta")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;

    @Column(name = "total_boleta")
    private int total;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;
}
