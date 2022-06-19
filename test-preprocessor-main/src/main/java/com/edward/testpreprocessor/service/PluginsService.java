package com.edward.testpreprocessor.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface PluginsService {

    void setupPlugins(Document document, Element project);
}
