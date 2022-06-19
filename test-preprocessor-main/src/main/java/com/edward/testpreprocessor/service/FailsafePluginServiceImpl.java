package com.edward.testpreprocessor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class FailsafePluginServiceImpl implements FailsafePluginService {

    @Value("${project.failsafe_version}")
    public String versionValue;

    @Override
    public void setup(Element plugins, Document document) {

        NodeList artifacts = plugins.getElementsByTagName("artifactId");
        Element failsafe;

        if (artifacts.getLength() > 0) {

            for (int i = 0; i < artifacts.getLength(); i++) {
                Node artifact = artifacts.item(i);
                if (artifact.getTextContent().equals("maven-failsafe-plugin")) {
                    return;
                }
            }

            failsafe = document.createElement("plugin");
            plugins.appendChild(failsafe);

        } else {
            failsafe = document.createElement("plugin");
            plugins.appendChild(failsafe);
        }

        Element artifact = document.createElement("artifactId");
        artifact.setTextContent("maven-failsafe-plugin");
        failsafe.appendChild(artifact);

        Element version = document.createElement("version");
        version.setTextContent(versionValue);
        failsafe.appendChild(version);
    }
}
