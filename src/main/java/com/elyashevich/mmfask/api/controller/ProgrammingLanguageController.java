package com.elyashevich.mmfask.api.controller;

import com.elyashevich.mmfask.api.dto.programmingLanguage.ProgrammingLanguageDto;
import com.elyashevich.mmfask.api.mapper.ProgrammingLanguageMapper;
import com.elyashevich.mmfask.service.ProgrammingLanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/programming-languages")
@RequiredArgsConstructor
public class ProgrammingLanguageController {

    private final ProgrammingLanguageService programmingLanguageService;
    private final ProgrammingLanguageMapper programmingLanguageMapper;

    @GetMapping
    public List<ProgrammingLanguageDto> findAll() {
        var programmingLanguages = this.programmingLanguageService.findAll();
        return this.programmingLanguageMapper.toDto(programmingLanguages);
    }

    @GetMapping("/{id}")
    public ProgrammingLanguageDto findById(final @PathVariable("id") String id) {
        var programmingLanguage = this.programmingLanguageService.findById(id);
        return this.programmingLanguageMapper.toDto(programmingLanguage);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProgrammingLanguageDto create(final @Validated @RequestBody ProgrammingLanguageDto programmingLanguageDto) {
        var programmingLanguage = this.programmingLanguageService.create(
                this.programmingLanguageMapper.toEntity(programmingLanguageDto)
        );
        return this.programmingLanguageMapper.toDto(programmingLanguage);
    }

    @PutMapping("/{id}")
    public ProgrammingLanguageDto update(
            final @PathVariable("id") String id,
            final @Validated @RequestBody ProgrammingLanguageDto programmingLanguageDto
    ) {
        var programmingLanguage = this.programmingLanguageService.update(
                id,
                this.programmingLanguageMapper.toEntity(programmingLanguageDto)
        );
        return this.programmingLanguageMapper.toDto(programmingLanguage);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(final @PathVariable("id") String id) {
        this.programmingLanguageService.delete(id);
    }
}
