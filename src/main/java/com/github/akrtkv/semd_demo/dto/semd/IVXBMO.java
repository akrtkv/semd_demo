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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IVXB_MO")
public class IVXBMO
        extends MO {

    @XmlAttribute(name = "inclusive")
    protected Boolean inclusive;

    public boolean isInclusive() {
        if (inclusive == null) {
            return true;
        } else {
            return inclusive;
        }
    }

    public void setInclusive(Boolean value) {
        this.inclusive = value;
    }
}
