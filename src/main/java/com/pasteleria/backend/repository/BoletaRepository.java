package com.pasteleria.backend.repository;

import com.pasteleria.backend.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    List<Boleta> findByUsuario_Id(Long idUsuario);
}
