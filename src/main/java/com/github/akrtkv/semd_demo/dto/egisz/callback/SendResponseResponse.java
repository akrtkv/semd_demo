package com.github.akrtkv.semd_demo.dto.egisz.callback;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendResponseResponse", propOrder = {
        "status"
})
public class SendResponseResponse {

    protected int status;

    public SendResponseResponse() {
    }

    public SendResponseResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int value) {
        this.status = value;
    }

    @Override
    public String toString() {
        return "SendResponseResponse{" +
                "status=" + status +
                '}';
    }
}
