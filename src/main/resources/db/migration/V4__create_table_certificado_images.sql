CREATE TABLE certificado_images (
    id BIGINT NOT NULL AUTO_INCREMENT,
    path VARCHAR(255),
    content_type VARCHAR(255),
    verso BIT(1),
    certificado_id BIGINT,
    created DATETIME(6),
    PRIMARY KEY (id),
    CONSTRAINT fk_certificado_images_certificado FOREIGN KEY (certificado_id) REFERENCES certificados (id)
);
