//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2022.01.20 at 04:56:34 PM MSK
//

package com.github.akrtkv.semd_demo.dto.semd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "POCD_MT000040.Section", propOrder = {
        "code",
        "title",
        "text",
        "entry"
})
public class POCDMT000040Section {

    @XmlElement(required = true)
    protected Code code;

    @XmlElement(required = true)
    protected String title;

    @XmlElement(required = true)
    protected StrucDocText text;

    @XmlElement(required = true)
    protected List<POCDMT000040Entry> entry;

    public Code getCode() {
        return code;
    }

    public void setCode(Code value) {
        this.code = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String value) {
        this.title = value;
    }

    public StrucDocText getText() {
        return text;
    }

    public void setText(StrucDocText value) {
        this.text = value;
    }

    public List<POCDMT000040Entry> getEntry() {
        if (entry == null) {
            entry = new ArrayList<POCDMT000040Entry>();
        }
        return this.entry;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Code
            extends CENonElements {

    }
}
