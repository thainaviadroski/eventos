package net.eventos_facu.eventos.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
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


    @ManyToMany(mappedBy = "certificados", fetch = FetchType.LAZY)
    private Set<Academicos> academicos = new HashSet<>();

    @Column(updatable = false)
    private String createdBy;

    @Column(updatable = false)
    private Instant created;
}