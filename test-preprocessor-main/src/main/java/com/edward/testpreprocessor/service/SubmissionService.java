package com.edward.testpreprocessor.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface SubmissionService {

    void submitProject(MultipartFile project) throws Exception;
}
