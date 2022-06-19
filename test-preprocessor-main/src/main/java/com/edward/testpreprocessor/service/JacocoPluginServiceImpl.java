package com.edward.testpreprocessor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class JacocoPluginServiceImpl implements JacocoPluginService {

    @Value("${project.jacoco_version}")
    public String jacocoVersion;

    @Override
    public void setup(Element plugins, Document document) {

        NodeList artifacts = plugins.getElementsByTagName("artifactId");
        Element jacoco;
        Node existsPlugin = null;

        if (artifacts.getLength() > 0) {

            for (int i = 0; i < artifacts.getLength(); i++) {
                Node artifact = artifacts.item(i);
                if (artifact.getTextContent().equals("jacoco-maven-plugin")) {
                    existsPlugin = artifact.getParentNode();
                    break;
                }
            }

            if (existsPlugin != null) {
                plugins.removeChild(existsPlugin);
            }

            jacoco = document.createElement("plugin");
            plugins.appendChild(jacoco);

        } else {
            jacoco = document.createElement("plugin");
            plugins.appendChild(jacoco);
        }

        buildJacocoPlugin(jacoco, document);
    }

    private void buildJacocoPlugin(Element jacoco, Document document) {

        Element groupId = document.createElement("groupId");
        groupId.setTextContent("org.jacoco");

        Element artifactId = document.createElement("artifactId");
        artifactId.setTextContent("jacoco-maven-plugin");

        Element version = document.createElement("version");
        version.setTextContent(jacocoVersion);

        Element executions = getJacocoExecutionsConfig(document);

        jacoco.appendChild(groupId);
        jacoco.appendChild(artifactId);
        jacoco.appendChild(version);
        jacoco.appendChild(executions);
    }

    private Element getJacocoExecutionsConfig(Document document) {

        Element executions = document.createElement("executions");

        Element prepare = getPrepareExecution(document);

        executions.appendChild(prepare);

        Element unitTest = getUnitTestExecution(document);

        executions.appendChild(unitTest);

        Element integrationTest = getIntegrationTestExecution(document);

        executions.appendChild(integrationTest);

        return executions;
    }

    private Element getPrepareExecution(Document document) {

        Element execution = document.createElement("execution");

        Element id = document.createElement("id");
        id.setTextContent("default-prepare");

        execution.appendChild(id);

        Element goals = document.createElement("goals");

        execution.appendChild(goals);

        Element goal = document.createElement("goal");
        goal.setTextContent("prepare-agent");

        goals.appendChild(goal);

        return execution;
    }

    private Element getUnitTestExecution(Document document) {

        Element execution = document.createElement("execution");

        Element id = document.createElement("id");
        id.setTextContent("jacoco-unit-test");

        execution.appendChild(id);

        Element phase = document.createElement("phase");
        phase.setTextContent("post-site");

        execution.appendChild(phase);

        Element goals = document.createElement("goals");

        execution.appendChild(goals);

        Element goal = document.createElement("goal");

        goal.setTextContent("report");

        goals.appendChild(goal);

        return execution;
    }

    private Element getIntegrationTestExecution(Document document) {

        Element execution = document.createElement("execution");

        Element id = document.createElement("id");
        id.setTextContent("integration-test");

        execution.appendChild(id);

        Element phase = document.createElement("phase");
        phase.setTextContent("integration-test");

        execution.appendChild(phase);

        Element goals = document.createElement("goals");

        execution.appendChild(goals);

        Element goal = document.createElement("goal");
        goal.setTextContent("report");

        goals.appendChild(goal);

        return execution;
    }
}
