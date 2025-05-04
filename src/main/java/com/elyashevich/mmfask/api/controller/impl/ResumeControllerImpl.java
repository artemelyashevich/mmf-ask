package com.elyashevich.mmfask.api.controller.impl;

import com.elyashevich.mmfask.api.controller.ResumeController;
import com.elyashevich.mmfask.api.dto.resume.ResumeCreateDto;
import com.elyashevich.mmfask.api.dto.resume.ResumeResponseDto;
import com.elyashevich.mmfask.api.mapper.impl.ResumeMapper;
import com.elyashevich.mmfask.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ResumeControllerImpl implements ResumeController {

    private final ResumeService resumeService;
    private final ResumeMapper resumeMapper;

    @Override
    public ResumeResponseDto findById(String id) {
        var resume = this.resumeService.findById(id);
        return this.resumeMapper.toDto(resume);
    }

    @Override
    public ResumeResponseDto findByUserId(String userId) {
        var resume = this.resumeService.findByUserId(userId);
        return this.resumeMapper.toDto(resume);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public ResumeResponseDto create(ResumeCreateDto dto) {
        var resume = this.resumeService.create(this.resumeMapper.toEntity(dto));
        return this.resumeMapper.toDto(resume);
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public ResumeResponseDto update(String id, ResumeCreateDto dto) {
        var resume = this.resumeService.update(id, this.resumeMapper.toEntity(dto));
        return this.resumeMapper.toDto(resume);
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(String id) {
        this.resumeService.delete(id);
    }
}
