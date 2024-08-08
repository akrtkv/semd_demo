//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2022.01.20 at 04:56:34 PM MSK
//

package com.github.akrtkv.semd_demo.dto.semd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "POCD_MT000040.Material", propOrder = {
        "code",
        "name"
})
public class POCDMT000040Material {

    protected Code code;

    protected String name;

    @XmlAttribute(name = "classCode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String classCode;

    @XmlAttribute(name = "determinerCode")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String determinerCode;

    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;

    public Code getCode() {
        return code;
    }

    public void setCode(Code value) {
        this.code = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String value) {
        this.classCode = value;
    }

    public String getDeterminerCode() {
        return determinerCode;
    }

    public void setDeterminerCode(String value) {
        this.determinerCode = value;
    }

    public List<String> getNullFlavor() {
        if (nullFlavor == null) {
            nullFlavor = new ArrayList<String>();
        }
        return this.nullFlavor;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Code
            extends CENonElements {

    }
}
