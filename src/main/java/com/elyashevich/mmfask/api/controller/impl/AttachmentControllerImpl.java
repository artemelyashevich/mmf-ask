package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.AttachmentController;
import com.elyashevich.mmfask.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AttachmentControllerImpl implements AttachmentController {

    private final AttachmentService attachmentService;

    @Override
    public byte[] download(final @PathVariable("id") String id) {
        var attachment = this.attachmentService.findById(id);
        return attachment.getBytes();
    }
}