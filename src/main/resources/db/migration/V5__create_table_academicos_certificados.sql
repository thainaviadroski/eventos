CREATE TABLE academicos_certificados (
    id BINARY(16) NOT NULL,
    academico_id BIGINT,
    certificado_id BIGINT,
    created DATETIME(6),
    ch INT,
    PRIMARY KEY (id),
    CONSTRAINT fk_academicos_certificados_academico FOREIGN KEY (academico_id) REFERENCES academicos (id),
    CONSTRAINT fk_academicos_certificados_certificado FOREIGN KEY (certificado_id) REFERENCES certificados (id)
);
