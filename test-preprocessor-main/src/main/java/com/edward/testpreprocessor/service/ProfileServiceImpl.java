package com.edward.testpreprocessor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Value("${project.pitest.profile_version}")
    public String pitestProfileVersion;

    @Value("${project.pitest.threads}")
    public String pitestThreads;

    private static final String THRESHOLD = "100";

    private static final String WEIGHT = "STRONGER";

    @Override
    public void setupProfile(Document document, Element project) {

        Element profiles;
        NodeList list = project.getElementsByTagName("profiles");

        if (list.getLength() > 0) {
            profiles = (Element)list.item(0);
        } else {
            profiles = document.createElement("profiles");
            project.appendChild(profiles);
        }

        buildPitestProfile(profiles, document);
    }

    private void buildPitestProfile(Element profiles, Document document) {

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
        version.setTextContent(pitestProfileVersion);

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
        threads.setTextContent(pitestThreads);

        configuration.appendChild(threads);

        Element timestampedReports = document.createElement("timestampedReports");
        timestampedReports.setTextContent("false");

        configuration.appendChild(timestampedReports);

        Element mutationThreshold = document.createElement("mutationThreshold");
        mutationThreshold.setTextContent(THRESHOLD);

        configuration.appendChild(mutationThreshold);

        Element mutators = document.createElement("mutators");
        mutators.setTextContent(WEIGHT);

        Element formats = document.createElement("outputFormats");
        configuration.appendChild(formats);

        Element xmlFormat = document.createElement("format");
        xmlFormat.setTextContent("XML");

        formats.appendChild(xmlFormat);

        Element htmlFormat = document.createElement("format");
        htmlFormat.setTextContent("HTML");

        formats.appendChild(htmlFormat);

        configuration.appendChild(mutators);
    }
}
