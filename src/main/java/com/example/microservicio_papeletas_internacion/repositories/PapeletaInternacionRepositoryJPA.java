package com.example.microservicio_papeletas_internacion.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.microservicio_papeletas_internacion.models.PapeletaInternacionEntity;

public interface PapeletaInternacionRepositoryJPA extends JpaRepository<PapeletaInternacionEntity, Integer> ,JpaSpecificationExecutor<PapeletaInternacionEntity>{
    @Query("SELECT ne FROM PapeletaInternacionEntity ne "
    + "JOIN ne.historiaClinica hc "
    + "JOIN hc.paciente p "
    + "WHERE p.idUsuario = :idPaciente")
    List<PapeletaInternacionEntity> obtenerNotasEvolucionPaciente(@Param("idPaciente") int idPaciente);
    Optional<PapeletaInternacionEntity> findByIdPapeletaDeInternacionAndDeletedAtIsNull(int idPapeletaDeInternacion);

    @Modifying
    @Query(value = "UPDATE papeletas_internacion SET deleted_at = ?2 WHERE id_historia_clinica = ?1", nativeQuery = true)
    void markAsDeletedAllPapeletasInternacionFromHistoriaClinica(int idHistoriaClinica, Date date);
}