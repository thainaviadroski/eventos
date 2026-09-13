CREATE TABLE certificados (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cabecalho VARCHAR(255),
    corpo TEXT,
    rodape VARCHAR(255),
    descricao TEXT,
    tipos_certificados VARCHAR(255),
    evento_id BIGINT,
    created_by VARCHAR(255),
    created DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_certificados_evento FOREIGN KEY (evento_id) REFERENCES eventos (id)
);
