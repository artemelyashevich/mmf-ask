package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/images/")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    @GetMapping(value = "{id}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public byte[] download(final @PathVariable("id") String id) {
        var attachment = this.attachmentService.findById(id);
        return attachment.getBytes();
    }
}
