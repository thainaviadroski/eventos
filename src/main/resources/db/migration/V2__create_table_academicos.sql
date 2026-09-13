CREATE TABLE academicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    academico VARCHAR(255),
    email VARCHAR(255),
    cpf VARCHAR(255),
    ra VARCHAR(255),
    created_by VARCHAR(255),
    created DATETIME(6),
    updated_by VARCHAR(255),
    updated DATETIME(6),
    PRIMARY KEY (id)
);
