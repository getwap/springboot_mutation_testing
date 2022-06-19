package com.edward.testpreprocessor.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface TypedPluginService {

    void setup(Element plugins, Document document);
}
