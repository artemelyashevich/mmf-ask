package com.elyashevich.mmfask.util;

import com.elyashevich.mmfask.entity.AttachmentImage;
import lombok.experimental.UtilityClass;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Optional;

@UtilityClass
public class ImageUtil {

    public static String generatePath(final AttachmentImage image) {
        Optional<AttachmentImage> imageOptional = Optional.ofNullable(image);
        return imageOptional
                .map(img ->
                        ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/api/v1/images/")
                                .path(img.getId())
                                .toUriString())
                .orElse(null);
    }
}
