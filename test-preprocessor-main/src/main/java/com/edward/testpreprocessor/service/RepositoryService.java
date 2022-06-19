package com.edward.testpreprocessor.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface RepositoryService {

    void setupRepository(Document document, Element project);
}
