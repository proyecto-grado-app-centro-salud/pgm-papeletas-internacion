package com.example.microservicio_papeletas_internacion.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservicio_papeletas_internacion.models.EspecialidadesEntity;

import java.util.List;
import java.util.Optional;

public interface EspecialidadesRepositoryJPA extends JpaRepository<EspecialidadesEntity, Integer> {
    List<EspecialidadesEntity> findAllByDeletedAtIsNull();
    Optional<EspecialidadesEntity> findByIdEspecialidadAndDeletedAtIsNull(int idEspecialidad);
}