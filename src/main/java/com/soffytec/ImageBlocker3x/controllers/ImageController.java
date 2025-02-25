package com.soffytec.ImageBlocker3x.controllers;

import com.soffytec.ImageBlocker3x.dto.ImageUploadRequest;
import com.soffytec.ImageBlocker3x.entities.ImageEntity;
import com.soffytec.ImageBlocker3x.exception.UnsafeContentException;
import com.soffytec.ImageBlocker3x.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageEntity> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            ImageUploadRequest request = new ImageUploadRequest();
            request.setFileName(file.getOriginalFilename());
            request.setImageData(file.getBytes());
            ImageEntity savedImage = imageService.uploadImage(request);
            return ResponseEntity.ok(savedImage);
        } catch (UnsafeContentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}