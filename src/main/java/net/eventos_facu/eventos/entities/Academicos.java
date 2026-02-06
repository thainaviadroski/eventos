package net.eventos_facu.eventos.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
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


    @OneToMany(mappedBy = "academico")
    private List<AcademicosCertificados> certificados = new ArrayList<>();
}
