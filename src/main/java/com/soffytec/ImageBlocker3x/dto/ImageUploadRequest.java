package com.soffytec.ImageBlocker3x.dto;

public class ImageUploadRequest {
    private String fileName;
    private byte[] imageData;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}