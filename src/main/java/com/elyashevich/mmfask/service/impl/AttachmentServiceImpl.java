package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.AttachmentImage;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.AttachmentImageRepository;
import com.elyashevich.mmfask.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentImageRepository attachmentImageRepository;

    @Override
    public AttachmentImage findByName(final String name) {
        return this.attachmentImageRepository.findByFilename(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Image with name = %s was not found.".formatted(name))
                );
    }

    @Override
    public AttachmentImage findById(final String id) {
        return this.attachmentImageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image with id = %s was not found.".formatted(id)));
    }

    @Override
    public AttachmentImage create(final MultipartFile file) throws Exception {
        log.debug("Attempting to create a new image");

        var fileName = UUID.randomUUID() + file.getOriginalFilename();
        try {
            var attachment = AttachmentImage.builder()
                    .filename(fileName)
                    .filetype(file.getContentType())
                    .bytes(file.getBytes())
                    .build();
            var image = this.attachmentImageRepository.save(attachment);

            log.info("Image with ID '{}' has been created.", attachment.getId());
            return image;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not save File: " + fileName);
        }
    }

    @Override
    public void delete(final String id) {
        log.debug("Attempting to delete the image with ID '{}'.", id);

        var image = this.findById(id);
        this.attachmentImageRepository.delete(image);

        log.info("Image with ID '{}' has been deleted.", id);
    }
}
