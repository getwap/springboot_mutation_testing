package com.edward.testpreprocessor.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Service
public class PluginsServiceImpl implements PluginsService {

    private final SurefirePluginService surefirePluginService;
    private final FailsafePluginService failsafePluginService;
    private final PitestPluginService pitestPluginService;
    private final JacocoPluginService jacocoPluginService;

    public PluginsServiceImpl(SurefirePluginService surefirePluginService, FailsafePluginService failsafePluginService, PitestPluginService pitestPluginService, JacocoPluginService jacocoPluginService) {
        this.surefirePluginService = surefirePluginService;
        this.failsafePluginService = failsafePluginService;
        this.pitestPluginService = pitestPluginService;
        this.jacocoPluginService = jacocoPluginService;
    }

    @Override
    public void setupPlugins(Document document, Element project) {

        Element build = getOrCreateBuild(document, project);

        NodeList list = build.getElementsByTagName("plugins");
        Element plugins;

        if (list.getLength() > 0) {
            plugins = (Element)list.item(0);
        } else {
            plugins = document.createElement("plugins");
            build.appendChild(plugins);
        }

        surefirePluginService.setup(plugins, document);

        failsafePluginService.setup(plugins, document);

        pitestPluginService.setup(plugins, document);

        jacocoPluginService.setup(plugins, document);
    }

    private Element getOrCreateBuild(Document document, Element project) {

        NodeList list = project.getElementsByTagName("build");
        Element build;

        if (list.getLength() > 0) {
            build = (Element)list.item(0);
        } else {
            build = document.createElement("build");
            project.appendChild(build);
        }

        return build;
    }
}
