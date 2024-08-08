//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2022.01.20 at 04:56:34 PM MSK
//

package com.github.akrtkv.semd_demo.dto.semd.identity;

import su.medsoft.rir.recipe.dto.iemk_new_edition.CENonElements;
import su.medsoft.rir.recipe.dto.iemk_new_edition.ST;
import su.medsoft.rir.recipe.dto.iemk_new_edition.STNonNF;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "POCD_MT000040.InsurancePolicy", propOrder = {
        "insurancePolicyType",
        "series",
        "number"
})
public class POCDMT000040InsurancePolicy {

    @XmlElement(name = "InsurancePolicyType")
    protected InsurancePolicyType insurancePolicyType;

    @XmlElement(name = "Series")
    protected ST series;

    @XmlElement(name = "Number")
    protected STNonNF number;

    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;

    public InsurancePolicyType getInsurancePolicyType() {
        return insurancePolicyType;
    }

    public void setInsurancePolicyType(InsurancePolicyType value) {
        this.insurancePolicyType = value;
    }

    public ST getSeries() {
        return series;
    }

    public void setSeries(ST value) {
        this.series = value;
    }

    public STNonNF getNumber() {
        return number;
    }

    public void setNumber(STNonNF value) {
        this.number = value;
    }

    public List<String> getNullFlavor() {
        if (nullFlavor == null) {
            nullFlavor = new ArrayList<String>();
        }
        return this.nullFlavor;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class InsurancePolicyType
            extends CENonElements {

    }
}
