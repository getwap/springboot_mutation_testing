package com.edward.testpreprocessor.service;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface PackagingService {

    void unzip(String filePath, String directoryPath) throws IOException;
}
