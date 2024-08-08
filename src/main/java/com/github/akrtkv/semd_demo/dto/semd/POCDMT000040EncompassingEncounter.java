//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2022.01.20 at 04:56:34 PM MSK
//

package com.github.akrtkv.semd_demo.dto.semd;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "POCD_MT000040.EncompassingEncounter", propOrder = {
        "id",
        "code",
        "docType",
        "effectiveTime"
})
public class POCDMT000040EncompassingEncounter {

    @XmlElement(required = true)
    protected List<Id> id;

    protected CE code;

    @XmlElement(name = "DocType")
    protected ST docType;

    @XmlElement(required = true)
    protected EffectiveTime effectiveTime;

    public List<Id> getId() {
        if (id == null) {
            id = new ArrayList<Id>();
        }
        return this.id;
    }

    public CE getCode() {
        return code;
    }

    public void setCode(CE value) {
        this.code = value;
    }

    public ST getDocType() {
        return docType;
    }

    public void setDocType(ST value) {
        this.docType = value;
    }

    public EffectiveTime getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(EffectiveTime value) {
        this.effectiveTime = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "low",
            "high"
    })
    public static class EffectiveTime {

        protected Low low;

        protected IVXBTS high;

        @XmlAttribute(name = "nullFlavor")
        protected List<String> nullFlavor;

        public Low getLow() {
            return low;
        }

        public void setLow(Low value) {
            this.low = value;
        }

        public IVXBTS getHigh() {
            return high;
        }

        public void setHigh(IVXBTS value) {
            this.high = value;
        }

        public List<String> getNullFlavor() {
            if (nullFlavor == null) {
                nullFlavor = new ArrayList<String>();
            }
            return this.nullFlavor;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "")
        public static class Low
                extends IVXBTS {

        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Id
            extends II {

    }
}
