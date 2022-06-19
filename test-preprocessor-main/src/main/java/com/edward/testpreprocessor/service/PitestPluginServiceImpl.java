package com.edward.testpreprocessor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class PitestPluginServiceImpl implements PitestPluginService {

    @Value("${project.pitest.version}")
    public String pitestVersion;

    @Value("${project.pitest.junit_version}")
    public String pitestJunitVersion;

    @Override
    public void setup(Element plugins, Document document) {

        NodeList artifacts = plugins.getElementsByTagName("artifactId");
        Element pitest;
        Node existsPlugin = null;

        if (artifacts.getLength() > 0) {

            for (int i = 0; i < artifacts.getLength(); i++) {
                Node artifact = artifacts.item(i);
                if (artifact.getTextContent().equals("pitest-maven")) {
                    existsPlugin = artifact.getParentNode();
                    break;
                }
            }

            if (existsPlugin != null) {
                plugins.removeChild(existsPlugin);
            }

            pitest = document.createElement("plugin");
            plugins.appendChild(pitest);

        } else {
            pitest = document.createElement("plugin");
            plugins.appendChild(pitest);
        }

        buildPitestPlugin(pitest, document);
    }

    private void buildPitestPlugin(Element pitest, Document document) {

        Element groupId = document.createElement("groupId");
        groupId.setTextContent("org.pitest");

        Element artifactId = document.createElement("artifactId");
        artifactId.setTextContent("pitest-maven");

        Element version = document.createElement("version");
        version.setTextContent(pitestVersion);

        Element dependencies = document.createElement("dependencies");

        Element dependency = document.createElement("dependency");
        dependencies.appendChild(dependency);

        Element depGroupId = document.createElement("groupId");
        depGroupId.setTextContent("org.pitest");

        Element depArtifactId = document.createElement("artifactId");
        depArtifactId.setTextContent("pitest-junit5-plugin");

        Element depVersion = document.createElement("version");
        depVersion.setTextContent(pitestJunitVersion);

        dependency.appendChild(depGroupId);
        dependency.appendChild(depArtifactId);
        dependency.appendChild(depVersion);

        pitest.appendChild(groupId);
        pitest.appendChild(artifactId);
        pitest.appendChild(version);
        pitest.appendChild(dependencies);
    }
}
