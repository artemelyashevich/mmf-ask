package com.elyashevich.mmfask.service;

import com.elyashevich.mmfask.entity.Resume;
import com.elyashevich.mmfask.service.contract.CrudService;

public interface ResumeService extends CrudService<Resume> {

    Resume findByUserId(String userId);
}
