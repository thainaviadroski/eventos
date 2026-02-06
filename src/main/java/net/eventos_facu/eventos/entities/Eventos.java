package net.eventos_facu.eventos.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "eventos")
public class Eventos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String evento;

    @Column
    private String descricao;

    @Column
    private LocalDateTime dtInicio;

    @Column
    private LocalDateTime dtFinal;

    @Column
    private String slug;

    @Column(updatable = false)
    private String createdBy;

    @Column(updatable = false)
    private Instant created;

    private String updatedBy;

    @Column
    private Instant updated;

    @JsonManagedReference
    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY)
    @ElementCollection
    private Set<Certificados> certificados = new HashSet<>();

}