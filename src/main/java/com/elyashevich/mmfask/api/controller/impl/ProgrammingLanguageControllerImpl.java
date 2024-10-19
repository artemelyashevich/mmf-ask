package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.ProgrammingLanguageController;
import com.elyashevich.mmfask.api.dto.programmingLanguage.ProgrammingLanguageDto;
import com.elyashevich.mmfask.api.mapper.ProgrammingLanguageMapper;
import com.elyashevich.mmfask.service.ProgrammingLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProgrammingLanguageControllerImpl implements ProgrammingLanguageController {

    private final ProgrammingLanguageService programmingLanguageService;
    private final ProgrammingLanguageMapper programmingLanguageMapper;

    @Override
    public List<ProgrammingLanguageDto> findAll() {
        var programmingLanguages = this.programmingLanguageService.findAll();
        return this.programmingLanguageMapper.toDto(programmingLanguages);
    }

    @Override
    public ProgrammingLanguageDto findById(final @PathVariable("id") String id) {
        var programmingLanguage = this.programmingLanguageService.findById(id);
        return this.programmingLanguageMapper.toDto(programmingLanguage);
    }

    @Override
    public ProgrammingLanguageDto create(final @RequestBody @Validated ProgrammingLanguageDto programmingLanguageDto) {
        var programmingLanguage = this.programmingLanguageService.create(
                this.programmingLanguageMapper.toEntity(programmingLanguageDto)
        );
        return this.programmingLanguageMapper.toDto(programmingLanguage);
    }

    @Override
    public ProgrammingLanguageDto update(
            final @PathVariable("id") String id,
            final @RequestBody @Validated ProgrammingLanguageDto programmingLanguageDto
    ) {
        var programmingLanguage = this.programmingLanguageService.update(
                id,
                this.programmingLanguageMapper.toEntity(programmingLanguageDto)
        );
        return this.programmingLanguageMapper.toDto(programmingLanguage);
    }

    @Override
    public void delete(final @PathVariable("id") String id) {
        this.programmingLanguageService.delete(id);
    }
}