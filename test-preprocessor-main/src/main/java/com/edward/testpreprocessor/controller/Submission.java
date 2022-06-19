package com.edward.testpreprocessor.controller;

import com.edward.testpreprocessor.service.SubmissionService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/submissions")
public class Submission {

    private static final String FOLDER_NAME = "submit";

    private final SubmissionService submissionService;

    public Submission(SubmissionService submissionService) {
        this.submissionService = submissionService;
    }

    @GetMapping
    public String list() {
        return FOLDER_NAME + "/list";
    }

    @GetMapping("/new")
    public String upload() {

        return FOLDER_NAME + "/new";
    }

    @PostMapping("/new")
    public String processUpload(@RequestParam("project") MultipartFile project, RedirectAttributes attributes) throws Exception {

        try {
            submissionService.submitProject(project);
            attributes.addFlashAttribute("message", "Project uploaded successfully");
            return "redirect:/submissions";
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
            attributes.addFlashAttribute("message", ie.getMessage());
            return "redirect:/submissions/new";
        } catch (IOException ioe) {
            ioe.printStackTrace();
            attributes.addFlashAttribute("message", "Error when upload the project");
            return "redirect:/submissions/new";
        }
    }
}
