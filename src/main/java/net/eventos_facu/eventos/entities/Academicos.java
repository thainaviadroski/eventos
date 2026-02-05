package net.eventos_facu.eventos.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "academicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "certificados")
@EqualsAndHashCode(exclude = "certificados")
public class Academicos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String academico;

    @Column
    private String email;

    @Column
    private String cpf;

    @Column
    private String ra;

    @Column
    private String createdBy;

    @Column
    private Instant created;

    @Column
    private String updatedBy;

    @Column
    private Instant updated;

    @ManyToMany
    @JoinTable(
            name = "academico_certificado",
            joinColumns = @JoinColumn(name = "academico_id"),
            inverseJoinColumns = @JoinColumn(name = "certificado_id"))
    List<Certificados> certificados;
}
