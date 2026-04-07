package net.eventos_facu.eventos.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "certificado_images")
public class CertificadoImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String path;

    @Column
    private String contentType;

    @Column
    private Boolean verso;

    @ManyToOne
    @JoinColumn(name = "certificado_id")
    private Certificados certificado;

    @Column
    private LocalDateTime created;

}
