package com.github.akrtkv.semd_demo.dto.egisz.callback;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendResponse", propOrder = {
        "id",
        "response"
})
public class SendResponse {

    @XmlElement(required = true)
    protected String id;

    @XmlElement(required = true)
    protected String response;

    public String getId() {
        return id;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String value) {
        this.response = value;
    }

    @Override
    public String toString() {
        return "SendResponse{" +
                "id='" + id + '\'' +
                ", response='" + response + '\'' +
                '}';
    }
}
