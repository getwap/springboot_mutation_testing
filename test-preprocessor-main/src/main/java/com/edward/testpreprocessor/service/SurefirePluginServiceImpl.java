package com.edward.testpreprocessor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class SurefirePluginServiceImpl implements SurefirePluginService {

    @Value("${project.surefire_version}")
    public String versionValue;

    @Override
    public void setup(Element plugins, Document document) {

        NodeList artifacts = plugins.getElementsByTagName("artifactId");
        Element surfire;

        if (artifacts.getLength() > 0) {

            for (int i = 0; i < artifacts.getLength(); i++) {
                Node artifact = artifacts.item(i);
                if (artifact.getTextContent().equals("maven-surefire-plugin")) {
                    return;
                }
            }

            surfire = document.createElement("plugin");
            plugins.appendChild(surfire);

        } else {
            surfire = document.createElement("plugin");
            plugins.appendChild(surfire);
        }

        Element artifact = document.createElement("artifactId");
        artifact.setTextContent("maven-surefire-plugin");
        surfire.appendChild(artifact);

        Element version = document.createElement("version");
        version.setTextContent(versionValue);
        surfire.appendChild(version);
    }
}
