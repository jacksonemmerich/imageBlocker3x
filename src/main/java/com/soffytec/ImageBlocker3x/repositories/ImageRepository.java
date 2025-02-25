package com.soffytec.ImageBlocker3x.repositories;

import com.soffytec.ImageBlocker3x.entities.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
    // Métodos personalizados, se necessário
}
