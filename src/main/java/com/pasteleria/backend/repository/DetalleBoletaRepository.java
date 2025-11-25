package com.pasteleria.backend.repository;

import com.pasteleria.backend.model.DetalleBoleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Long> {
    List<DetalleBoleta> findByBoleta_Id(Long idBoleta);
}