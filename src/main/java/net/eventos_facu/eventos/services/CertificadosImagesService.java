package net.eventos_facu.eventos.services;

import net.eventos_facu.eventos.entities.CertificadoImages;
import net.eventos_facu.eventos.repositories.CertificadoImagesRepository;
import net.eventos_facu.eventos.utils.FileUtils;
import org.springframework.stereotype.Service;

@Service
public class CertificadosImagesService {

    private final CertificadoImagesRepository repository;

    private final FileUtils fileUtils;

    public CertificadosImagesService(CertificadoImagesRepository repository, FileUtils fileUtils) {
        this.repository = repository;
        this.fileUtils = fileUtils;
    }

    /*
    * Vamos validar se o certificado já possui uma imagem cadastrada se não houver permitir que salve uma imagem
    * Se houver validar se é o verso ou não
    * Se ja houver duas imagens um frente e um verso, retornar uma exception informando que o certificado já possui as imagens
    * */
    public void createNewCertificadoImages(){

    }

    private CertificadoImages save(CertificadoImages image){
       return repository.save(image);
    }

    //Vamos criar um nome novo para a imagem padronizando a nomeclatura e diferenciando a imagem de fundo da imagem do verso
    private String fileName(){
        return new String();
    }


}
