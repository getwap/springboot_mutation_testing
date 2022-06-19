package com.edward.testpreprocessor.service;

import com.edward.testpreprocessor.utils.StreamGobbler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    @Value("${project.upload_dir}")
    public String UPLOAD_DIR;

//    @Value("classpath:script.sh")
//    public Resource resource;

    private final PackagingService packagingService;

    private final PomConfigurationService pomConfigurationService;

    public SubmissionServiceImpl(PackagingService packagingService, PomConfigurationService pomConfigurationService) {
        this.packagingService = packagingService;
        this.pomConfigurationService = pomConfigurationService;
    }

    @Override
    public void submitProject(MultipartFile project) throws Exception {

        if (project.isEmpty()) {
            throw new IllegalArgumentException("Please select a project to upload");
        }

        String fileName = StringUtils.cleanPath(project.getOriginalFilename());

        if (!fileName.endsWith(".zip")) {
            throw new IllegalArgumentException("Please select a zip file to upload");
        }

        String newFileName = UUID.randomUUID().toString() + ".zip";

        File dir = new File(UPLOAD_DIR);

        if (!dir.exists()) {
            dir.mkdir();
        }

        Path path = Paths.get(UPLOAD_DIR + "/" + newFileName);
        Files.copy(project.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        configure(UPLOAD_DIR , fileName.replace(".zip", ""), newFileName, project.getInputStream());
    }

    private void configure(String dir, String originalName, String fileName, InputStream in) throws Exception {

        String fullPath = dir + "/" + fileName;
        String withOutExt = fileName.replace(".zip", "");

        packagingService.unzip(fullPath, UPLOAD_DIR + "/" + withOutExt);

        String fullDir = dir + "/" + withOutExt + "/" + originalName;

        File pom = new File(fullDir + "/pom.xml");

        if (pom.exists()) {
            System.out.println("The Pom File exists!!");
            editPomFile(pom);
         //   addScript(fullDir);
            runScript(fullDir);
        } else {
            System.out.println("The Pom file does not exists!!!!");
        }
    }

    private void editPomFile(File pom) {
        pomConfigurationService.setupTesting(pom);
    }

//    private void addScript(String dir) throws IOException {
//        Path path = Paths.get(dir + "/script.sh");
//        Files.copy(resource.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//    }

    private void runScript(String dir) throws IOException, InterruptedException {

        ProcessBuilder builder = new ProcessBuilder();
        builder.command("mvn", "-Ppitest", "integration-test");
        builder.directory(new File(dir));

        Process process = builder.start();

        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);

        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Ok exec");
        } else {
            System.out.println("Error exec");
        }
    }

    private void unzip(String fullPath, String name) throws IOException {
        File destDir = new File(UPLOAD_DIR + "/" + name);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(new FileInputStream(fullPath));
        ZipEntry zipEntry = zis.getNextEntry();
        System.out.println(zipEntry.getSize());
        while (zipEntry != null) {
            File newFile = newFile(destDir, zipEntry);
            if (zipEntry.isDirectory()) {
                if (!newFile.isDirectory() && !newFile.mkdirs()) {
                    throw new IOException("Failed to create directory " + newFile);
                }
            } else {
                // fix for Windows-created archives
                File parent = newFile.getParentFile();
                if (!parent.isDirectory() && !parent.mkdirs()) {
                    throw new IOException("Failed to create directory " + parent);
                }

                // write file content
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
            }
            zipEntry = zis.getNextEntry();
        }
        zis.closeEntry();
        zis.close();
    }

    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
