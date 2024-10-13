package com.example.microservicio_papeletas_internacion.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.microservicio_papeletas_internacion.models.HistoriaClinicaEntity;
import com.example.microservicio_papeletas_internacion.models.UsuarioEntity;

public interface HistoriaClinicaRepositoryJPA extends JpaRepository<HistoriaClinicaEntity, Integer> {

    List<HistoriaClinicaEntity> findByPaciente(UsuarioEntity paciente);
}