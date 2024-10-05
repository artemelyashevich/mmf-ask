package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.AttachmentImage;
import org.springframework.web.multipart.MultipartFile;

public interface AttachmentService {

    AttachmentImage findByName(final String name);

    AttachmentImage findById(final String id);

    AttachmentImage create(final MultipartFile file) throws Exception;

    void delete(final String id);
}
