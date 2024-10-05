package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.AttachmentImage;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service interface for managing attachments.
 */
public interface AttachmentService {

    /**
     * Finds an attachment by name.
     *
     * @param name the name of the attachment
     * @return the attachment image
     */
    AttachmentImage findByName(final String name);

    /**
     * Finds an attachment by ID.
     *
     * @param id the ID of the attachment
     * @return the attachment image
     */
    AttachmentImage findById(final String id);

    /**
     * Creates a new attachment from a multipart file.
     *
     * @param file the multipart file to create the attachment from
     * @return the created attachment image
     * @throws Exception if an error occurs during creation
     */
    AttachmentImage create(final MultipartFile file) throws Exception;

    /**
     * Deletes an attachment by ID.
     *
     * @param id the ID of the attachment to delete
     */
    void delete(final String id);
}
