package com.elyashevich.mmfask.service.impl;

import com.elyashevich.mmfask.entity.ProgrammingLanguage;
import com.elyashevich.mmfask.exception.ResourceAlreadyExistsException;
import com.elyashevich.mmfask.exception.ResourceNotFoundException;
import com.elyashevich.mmfask.repository.ProgrammingLanguageRepository;
import com.elyashevich.mmfask.service.ProgrammingLanguageService;
import com.elyashevich.mmfask.service.converter.impl.ProgrammingLanguageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgrammingLanguageServiceImpl implements ProgrammingLanguageService {

    private final ProgrammingLanguageRepository programmingLanguageRepository;
    private final ProgrammingLanguageConverter converter;

    @Override
    public List<ProgrammingLanguage> findAll() {
        return this.programmingLanguageRepository.findAll();
    }

    @Override
    public ProgrammingLanguage findByName(final String name) {
        return this.programmingLanguageRepository.findByName(name)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Programming language with name = %s was not found.".formatted(name)
                        )
                );
    }

    @Override
    public ProgrammingLanguage findById(final String id) {
        return this.programmingLanguageRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Programming language with id = %s was not found.".formatted(id)
                        )
                );
    }

    @Transactional
    @Override
    public ProgrammingLanguage create(final ProgrammingLanguage dto) {
        var name = dto.getName();
        if (this.programmingLanguageRepository.existsByName(name)) {
            throw new ResourceAlreadyExistsException(
                    "Programming language with name = %s already exists.".formatted(name)
            );
        }
        return this.programmingLanguageRepository.save(dto);
    }


    @Transactional
    @Override
    public ProgrammingLanguage update(final String id, final ProgrammingLanguage dto) {
        var candidate = this.findById(id);
        var programmingLanguage = this.converter.update(candidate, dto);
        return this.programmingLanguageRepository.save(programmingLanguage);
    }

    @Transactional
    @Override
    public void delete(final String id) {
        var candidate = this.findById(id);
        this.programmingLanguageRepository.delete(candidate);
    }
}
