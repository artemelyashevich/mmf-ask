package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.AttachmentImage;
import com.elyashevich.mmfask.entity.Post;
import org.springframework.web.multipart.MultipartFile;

public interface PostService extends Service<Post> {

    Post uploadFile(final String id, final MultipartFile file) throws Exception;
}
