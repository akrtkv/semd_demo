package com.github.akrtkv.semd_demo.dto.egisz.request;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendDocumentResponse", propOrder = {
        "id"
})
public class SendDocumentResponse {

    @XmlElement
    private String id;

    private String error;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
