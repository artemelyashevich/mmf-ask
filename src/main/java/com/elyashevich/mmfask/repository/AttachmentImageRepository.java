package com.elyashevich.mmfask.repository;

import com.elyashevich.mmfask.entity.AttachmentImage;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface AttachmentImageRepository extends MongoRepository<AttachmentImage, String> {

    Optional<AttachmentImage> findByFilename(final String name);

    boolean existsByFilename(final String name);
}
