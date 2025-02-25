package com.soffytec.ImageBlocker3x.service;

import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;
import com.soffytec.ImageBlocker3x.dto.ImageUploadRequest;
import com.soffytec.ImageBlocker3x.entities.ImageEntity;
import com.soffytec.ImageBlocker3x.exception.UnsafeContentException;
import com.soffytec.ImageBlocker3x.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    Logger logger = LoggerFactory.getLogger(ImageService.class);


    public ImageEntity uploadImage(ImageUploadRequest request) throws IOException {
        try {
            if (!isImageSafe(request.getImageData())) {
                throw new UnsafeContentException("A imagem contém conteúdo impróprio (adulto, violência, etc.)");
            }
        } catch (Exception e) {
            // Tratar a exceção aqui
            logger.error("Erro ao verificar a segurança da imagem", e);
            throw new RuntimeException("Erro ao verificar a segurança da imagem", e);
        }

        // Salvar arquivo no sistema de arquivos (exemplo simples)
        String uploadDir = "/home/jackson/Documentos/imageblocker3x/";
        Files.createDirectories(Paths.get(uploadDir));
        Path filePath = Paths.get(uploadDir + request.getFileName());
        Files.write(filePath, request.getImageData());

        // Salvar no banco
        ImageEntity image = new ImageEntity();
        image.setFileName(request.getFileName());
        image.setFilePath(filePath.toString());
        return imageRepository.save(image);
    }

    private boolean isImageSafe(byte[] imageData) throws IOException {
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            ByteString imgBytes = ByteString.copyFrom(imageData);
            com.google.cloud.vision.v1.Image img = com.google.cloud.vision.v1.Image.newBuilder()
                    .setContent(imgBytes)
                    .build();

            Feature feat = Feature.newBuilder().setType(Type.SAFE_SEARCH_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(feat)
                    .setImage(img)
                    .build();

            List<AnnotateImageRequest> requests = new ArrayList<>();
            requests.add(request);

            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            AnnotateImageResponse res = response.getResponsesList().get(0);

            if (res.hasError()) {
                throw new IOException("Erro ao processar a imagem: " + res.getError().getMessage());
            }

            SafeSearchAnnotation safeSearch = res.getSafeSearchAnnotation();
            return !isContentUnsafe(safeSearch);
        }
    }

    private boolean isContentUnsafe(SafeSearchAnnotation safeSearch) {
        return safeSearch.getAdultValue() >= Likelihood.LIKELY.getNumber() ||
                safeSearch.getViolenceValue() >= Likelihood.LIKELY.getNumber() ||
                safeSearch.getRacyValue() >= Likelihood.LIKELY.getNumber() ||
                safeSearch.getMedicalValue() >= Likelihood.LIKELY.getNumber() ||
                safeSearch.getSpoofValue() >= Likelihood.LIKELY.getNumber();
    }
}