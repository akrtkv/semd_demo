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
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PIVL_TS", propOrder = {
        "phase",
        "period"
})
public class PIVLTS
        extends SXCMTS {

    protected IVLTS phase;

    protected PQ period;

    @XmlAttribute(name = "alignment")
    protected List<String> alignment;

    @XmlAttribute(name = "institutionSpecified")
    protected Boolean institutionSpecified;

    public IVLTS getPhase() {
        return phase;
    }

    public void setPhase(IVLTS value) {
        this.phase = value;
    }

    public PQ getPeriod() {
        return period;
    }

    public void setPeriod(PQ value) {
        this.period = value;
    }

    public List<String> getAlignment() {
        if (alignment == null) {
            alignment = new ArrayList<String>();
        }
        return this.alignment;
    }

    public boolean isInstitutionSpecified() {
        if (institutionSpecified == null) {
            return false;
        } else {
            return institutionSpecified;
        }
    }

    public void setInstitutionSpecified(Boolean value) {
        this.institutionSpecified = value;
    }
}
