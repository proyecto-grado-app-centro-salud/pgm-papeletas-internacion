package com.example.microservicio_papeletas_internacion.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "historias_clinicas")
public class HistoriaClinicaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historia_clinica")
    private int idHistoriaClinica;
    @Column(name = "condiciones_actuales_estado_salud_enfermedad")
    private String amnesis;
    @Column(name = "antecedentes_familiares")
    private String antecedentesFamiliares;
    @Column(name = "antecedentes_ginecoobstetricos")
    private String antecedentesGinecoobstetricos;
    @Column(name = "antecedentes_no_patologicos")
    private String antecedentesNoPatologicos;
    @Column(name = "antecedentes_patologicos")
    private String antecedentesPatologicos;
    @Column(name = "antecedentes_personales")
    private String antecedentesPersonales;
    @Column(name = "diagnostico_presuntivo")
    private String diagnosticoPresuntivo;
    @Column(name = "diagnosticos_diferenciales")
    private String diagnosticosDiferenciales;
    @Column(name = "examen_fisico_general")
    private String examenFisico;
    @Column(name = "examen_fisico_especial")
    private String examenFisicoEspecial;
    @Column(name = "propuesta_basica_de_conducta")
    private String propuestaBasicaDeConducta;
    @Column(name = "tratamiento")
    private String tratamiento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private UsuarioEntity paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medico", nullable = false)
    private UsuarioEntity medico;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_especialidad", nullable = false)
    private EspecialidadesEntity especialidad;
  
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Date updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deleted_at")
    private Date deletedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

    public void markAsDeleted() {
        deletedAt = new Date();
    }

}
