package com.github.akrtkv.semd_demo.dto.egisz.request;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendDocument", propOrder = {
        "service",
        "document"
})
public class SendDocument {

    @XmlElement(required = true)
    protected String service;

    @XmlElement(required = true)
    protected String document;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }
}
