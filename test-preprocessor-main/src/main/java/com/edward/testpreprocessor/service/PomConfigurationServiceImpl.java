package com.edward.testpreprocessor.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class PomConfigurationServiceImpl implements PomConfigurationService {

    private static final String FORMAT_XSLT = "src/main/resources/xslt/staff-format.xslt";

    private final EncodingService encodingService;
    private final PluginsService pluginsService;
    private final ProfileService profileService;
    private final RepositoryService repositoryService;

    public PomConfigurationServiceImpl(EncodingService encodingService, PluginsService pluginsService, ProfileService profileService, RepositoryService repositoryService) {
        this.encodingService = encodingService;
        this.pluginsService = pluginsService;
        this.profileService = profileService;
        this.repositoryService = repositoryService;
    }

    @Override
    public void setupTesting(File pom) {

        if (!pom.exists()) {
            throw new IllegalArgumentException("The file does not exists");
        }

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        try {
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();

            Document document = builder.parse(pom);

            document.getDocumentElement().normalize();

            Element project = (Element) document.getElementsByTagName("project").item(0);

            encodingService.setupEncoding(document, project);

            pluginsService.setupPlugins(document, project);

            profileService.setupProfile(document, project);

            repositoryService.setupRepository(document, project);

            try(FileOutputStream fileOutputStream = new FileOutputStream(pom.getAbsoluteFile())) {
                write(document, fileOutputStream);
            } catch (TransformerException e) {
                e.printStackTrace();
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(Document document, OutputStream output) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        // The default add many empty new line, not sure why?
        // https://mkyong.com/java/pretty-print-xml-with-java-dom-and-xslt/
        // Transformer transformer = transformerFactory.newTransformer();

        // add a xslt to remove the extra newlines
        Transformer transformer = transformerFactory.newTransformer();

        // pretty print
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }

   /* private void setupRepository(Document document) {

        Element repositories;
        NodeList list = document.getElementsByTagName("repositories");

        if (list.getLength() > 0) {
            repositories = (Element)list.item(0);
        } else {
            repositories = document.createElement("repositories");
            document.appendChild(repositories);
        }

        Element repository = document.createElement("repository");

        repositories.appendChild(repository);

        Element id = document.createElement("id");
        id.setTextContent("pitest-repo");

        repository.appendChild(id);

        Element url = document.createElement("url");
        url.setTextContent("https://jarcasting.com/artifacts/org.pitest/pitest-junit5-plugin/");

        repository.appendChild(url);
    } */

  /*  private void setupProfile(Document document) {

        Element profiles;
        NodeList list = document.getElementsByTagName("profiles");

        if (list.getLength() > 0) {
            profiles = (Element)list.item(0);
        } else {
            profiles = document.createElement("profiles");
            document.appendChild(profiles);
        }

        buildPitestProfile(profiles, document);
    } */

   /* private void buildPitestProfile(Element profiles, Document document) {

        Element profile = document.createElement("profile");

        profiles.appendChild(profile);

        Element id = document.createElement("id");
        id.setTextContent("pitest");

        profile.appendChild(id);

        Element build = document.createElement("build");

        profile.appendChild(build);

        Element plugins = document.createElement("plugins");

        build.appendChild(plugins);

        Element plugin = document.createElement("plugin");

        plugins.appendChild(plugin);

        Element groupId = document.createElement("groupId");
        groupId.setTextContent("org.pitest");

        plugin.appendChild(groupId);

        Element artifactId = document.createElement("artifactId");
        artifactId.setTextContent("pitest-maven");

        plugin.appendChild(artifactId);

        Element version = document.createElement("version");
        version.setTextContent("1.4.9");

        plugin.appendChild(version);

        Element executions = document.createElement("executions");

        plugin.appendChild(executions);

        Element execution = document.createElement("execution");

        executions.appendChild(execution);

        Element execId = document.createElement("id");
        execId.setTextContent("pitest");

        execution.appendChild(execId);

        Element execPhase = document.createElement("phase");
        execPhase.setTextContent("test");

        execution.appendChild(execPhase);

        Element goals = document.createElement("goals");

        execution.appendChild(goals);

        Element goal = document.createElement("goal");
        goal.setTextContent("mutationCoverage");

        goals.appendChild(goal);

        Element configuration = document.createElement("configuration");

        plugin.appendChild(configuration);

        Element threads = document.createElement("threads");
        threads.setTextContent("1");

        configuration.appendChild(threads);

        Element timestampedReports = document.createElement("timestampedReports");
        timestampedReports.setTextContent("false");

        configuration.appendChild(timestampedReports);

        Element mutationThreshold = document.createElement("mutationThreshold");
        mutationThreshold.setTextContent("100");

        configuration.appendChild(mutationThreshold);

        Element mutators = document.createElement("mutators");
        mutators.setTextContent("STRONGER");

        configuration.appendChild(mutators);
    } */

   /* private void setupEncoding(Document document) {

        Element properties;
        NodeList list = document.getElementsByTagName("properties");

        if (list.getLength() > 0) {
            properties = (Element)list.item(0);
        } else {
            properties = document.createElement("properties");
            document.appendChild(properties);
        }

        NodeList encodingList = properties.getElementsByTagName("project.build.sourceEncoding");

        if (encodingList.getLength() == 0) {
            Node encoding = document.createElement("project.build.sourceEncoding");
            encoding.setTextContent("UTF-8");
            properties.appendChild(encoding);
        }
    } */

  /*  private void setupPlugins(Document document) {

        NodeList list = document.getElementsByTagName("plugins");
        Element plugins;

        if (list.getLength() > 0) {
            plugins = (Element)list.item(0);
        } else {
            plugins = document.createElement("plugins");
            document.appendChild(plugins);
        }

        setupSurefirePlugin(plugins, document);

        setupFailsafePlugin(plugins, document);

        setupPitestPlugin(plugins, document);

        setupJacocoPlugin(plugins, document);

    } */

   /* private void setupSurefirePlugin(Element plugins, Document document) {

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
        version.setTextContent("2.22.2");
        surfire.appendChild(version);
    }

    private void setupFailsafePlugin(Element plugins, Document document) {

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
            document.appendChild(failsafe);

        } else {
            failsafe = document.createElement("plugin");
            plugins.appendChild(failsafe);
        }

        Element artifact = document.createElement("artifactId");
        artifact.setTextContent("maven-failsafe-plugin");
        failsafe.appendChild(artifact);

        Element version = document.createElement("version");
        version.setTextContent("2.22.2");
        failsafe.appendChild(version);
    }

    private void setupPitestPlugin(Element plugins, Document document) {

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
        version.setTextContent("1.4.9");

        Element dependencies = document.createElement("dependencies");

        Element dependency = document.createElement("dependency");
        dependencies.appendChild(dependency);

        Element depGroupId = document.createElement("groupId");
        depGroupId.setTextContent("org.pitest");

        Element depArtifactId = document.createElement("artifactId");
        depArtifactId.setTextContent("pitest-junit5-plugin");

        Element depVersion = document.createElement("version");
        depVersion.setTextContent("0.15");

        dependency.appendChild(depGroupId);
        dependency.appendChild(depArtifactId);
        dependency.appendChild(depVersion);

        pitest.appendChild(groupId);
        pitest.appendChild(artifactId);
        pitest.appendChild(version);
        pitest.appendChild(dependencies);
    }

    private void setupJacocoPlugin(Element plugins, Document document) {

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
        version.setTextContent("0.8.8");

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
    } */
}
