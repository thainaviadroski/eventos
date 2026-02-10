package net.eventos_facu.eventos.entities;

public enum TiposCertificados {
    PARTICIPANTE("Participante", "Discente/Docente que esteve presente no evento"),
    ORGANIZADOR("Organizador", "Discente/Docente que foi responsavel pela organização do evento"),
    PALESTRANTE("Palestrante", "Dicente/Docente/Convidado que ministrou palestra durante o evento"),
    COORDENADOR("Coordenador", "Coordenador de curso responsavel pelo evento");

    private final String name;
    private final String description;

    TiposCertificados(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
