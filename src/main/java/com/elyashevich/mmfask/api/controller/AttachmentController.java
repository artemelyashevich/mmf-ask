package com.elyashevich.mmfask.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for managing image attachments.
 */
@RequestMapping("/api/v1/images/")
@Tag(name = "Attachment Controller", description = "APIs for managing image attachments")
public interface AttachmentController {

    /**
     * Download an image file by ID.
     *
     * @param id The ID of the image to download.
     * @return Byte array representing the image file.
     */
    @Operation(
            summary = "Download image by ID",
            description = "Download image file by ID"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Image found",
            content = @Content(
                    mediaType = MediaType.IMAGE_JPEG_VALUE,
                    schema = @Schema(type = "string", format = "binary")
            )
    )
    @GetMapping(value = "{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] download(final @PathVariable("id") String id);
}