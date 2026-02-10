package net.eventos_facu.eventos.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "certificado_images")
public class CertitificadoImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String path;

    @Column
    private Boolean verso;

    @ManyToOne
    @JoinColumn(name = "certificado_id")
    private Certificados certificado;

    @Column
    private Instant created;


}
