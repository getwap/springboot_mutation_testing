package com.edward.testpreprocessor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class EncodingServiceImpl implements EncodingService {

    @Value("${project.encoding}")
    public String encodingValue;

    @Override
    public void setupEncoding(Document document, Element project) {
        Element properties;
        NodeList list = document.getElementsByTagName("properties");

        if (list.getLength() > 0) {
            properties = (Element)list.item(0);
        } else {
            properties = document.createElement("properties");
            project.appendChild(properties);
        }

        NodeList encodingList = properties.getElementsByTagName("project.build.sourceEncoding");

        if (encodingList.getLength() == 0) {
            Node encoding = document.createElement("project.build.sourceEncoding");
            encoding.setTextContent(encodingValue);
            properties.appendChild(encoding);
        }
    }
}
