package com.edward.testpreprocessor.service;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface EncodingService {

    void setupEncoding(Document document, Element project);
}
