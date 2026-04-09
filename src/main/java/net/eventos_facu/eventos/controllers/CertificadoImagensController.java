package net.eventos_facu.eventos.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/certficado-imagens")
public class CertificadoImagensController {

    /* TODO
     * Rotas para alteração das imagens elas serão responsaveis apenas para atualizar a imagem
     * Rota para remover as imagens
     * Rota para carregar as imagens
     * */

    @PutMapping(value = "/{imagemId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void update(@PathVariable Long imagemId, @RequestPart MultipartFile fundo) {

    }
    @PutMapping(value = "/{imagemId}/verso", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void updateVerso(@PathVariable Long imagemId, @RequestPart MultipartFile  verso) {

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

    }
}
