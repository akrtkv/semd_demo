//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2022.01.20 at 04:56:34 PM MSK
//

package com.github.akrtkv.semd_demo.dto.semd;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EN", propOrder = {
        "content"
})
@XmlSeeAlso({
        PN.class
})
public class EN {

    @XmlElementRefs({
            @XmlElementRef(name = "given", namespace = "urn:hl7-org:v3", type = JAXBElement.class),
            @XmlElementRef(name = "Patronymic", namespace = "urn:hl7-ru:identity", type = JAXBElement.class),
            @XmlElementRef(name = "family", namespace = "urn:hl7-org:v3", type = JAXBElement.class)
    })
    @XmlMixed
    protected List<Serializable> content;

    public List<Serializable> getContent() {
        if (content == null) {
            content = new ArrayList<Serializable>();
        }
        return this.content;
    }
}
