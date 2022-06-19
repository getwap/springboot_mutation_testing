package com.edward.testpreprocessor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Service
public class RepositoryServiceImpl implements RepositoryService {

    @Value("${project.pitest.repository}")
    public String repositoryUrl;

    @Override
    public void setupRepository(Document document, Element project) {

        Element repositories;
        NodeList list = document.getElementsByTagName("repositories");

        if (list.getLength() > 0) {
            repositories = (Element)list.item(0);
        } else {
            repositories = document.createElement("repositories");
            project.appendChild(repositories);
        }

        Element repository = document.createElement("repository");

        repositories.appendChild(repository);

        Element id = document.createElement("id");
        id.setTextContent("pitest-repo");

        repository.appendChild(id);

        Element url = document.createElement("url");
        url.setTextContent(repositoryUrl);

        repository.appendChild(url);
    }
}
