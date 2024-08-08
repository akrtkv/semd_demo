package com.github.akrtkv.semd_demo.dto.egisz.callback;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class UploadStatusElement {

    @XmlElement(name = "status_code")
    private String statusCode;

    @XmlElement(name = "docs_qty")
    private Integer docs;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getDocs() {
        return docs;
    }

    public void setDocs(Integer docs) {
        this.docs = docs;
    }
}
