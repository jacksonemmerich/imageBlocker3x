package com.soffytec.ImageBlocker3x.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    private String filePath; // Caminho onde a imagem foi salva

    private byte[] imageData; // Dados da imagem em bytes

    public ImageEntity() {

    }

    public ImageEntity(Long id, String fileName, String filePath, byte[] imageData) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.imageData = imageData;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    // Construtores
    // Outros métodos, se necessário

}
