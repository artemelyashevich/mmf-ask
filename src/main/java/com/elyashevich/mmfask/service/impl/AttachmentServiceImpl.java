package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.AttachmentImage;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.AttachmentImageRepository;
import com.elyashevich.mmfask.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

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
    public void delete(final String id) {
        var image = this.findById(id);
        this.attachmentImageRepository.delete(image);
    }

    @Override
    public AttachmentImage create(final MultipartFile file) throws Exception {
        var fileName = UUID.randomUUID() + file.getOriginalFilename();
        try {
            var attachment = AttachmentImage.builder()
                    .filename(fileName)
                    .filetype(file.getContentType())
                    .bytes(file.getBytes())
                    .build();
            return this.attachmentImageRepository.save(attachment);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not save File: " + fileName);
        }
    }
}
