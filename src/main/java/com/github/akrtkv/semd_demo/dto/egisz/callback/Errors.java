package com.github.akrtkv.semd_demo.dto.egisz.callback;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Errors {

    @XmlElement(name = "element")
    private java.lang.Error error;

    public java.lang.Error getError() {
        return error;
    }

    public void setError(java.lang.Error error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "Errors{" +
                "error=" + error +
                '}';
    }
}
