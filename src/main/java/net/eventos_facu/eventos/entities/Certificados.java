package net.eventos_facu.eventos.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "certificados")
public class Certificados {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String cabecalho;

    @Column
    private String corpo;

    @Column
    private String rodape;

    @Column
    private String descricao;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evento_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Eventos evento;


    @OneToMany(mappedBy = "certificado")
    private List<AcademicosCertificados> academicos = new ArrayList<>();

    @OneToMany(mappedBy = "certificado")
    private List<CertitificadoImages> imagens = new ArrayList<CertitificadoImages>();


    @Column(updatable = false)
    private String createdBy;

    @Column(updatable = false)
    private Instant created;
}