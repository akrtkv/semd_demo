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
@XmlType(name = "POCD_MT000040.SubstanceAdministration_1", propOrder = {
        "effectiveTime",
        "doseQuantity",
        "consumable"
})
public class POCDMT000040SubstanceAdministration1 {

    protected List<TS> effectiveTime;

    protected IVLPQ doseQuantity;

    @XmlElement(required = true)
    protected POCDMT000040Consumable consumable;

    @XmlAttribute(name = "classCode", required = true)
    protected List<String> classCode;

    @XmlAttribute(name = "moodCode", required = true)
    protected XDocumentSubstanceMood moodCode;

    public List<TS> getEffectiveTime() {
        if (effectiveTime == null) {
            effectiveTime = new ArrayList<TS>();
        }
        return this.effectiveTime;
    }

    public IVLPQ getDoseQuantity() {
        return doseQuantity;
    }

    public void setDoseQuantity(IVLPQ value) {
        this.doseQuantity = value;
    }

    public POCDMT000040Consumable getConsumable() {
        return consumable;
    }

    public void setConsumable(POCDMT000040Consumable value) {
        this.consumable = value;
    }

    public List<String> getClassCode() {
        if (classCode == null) {
            classCode = new ArrayList<String>();
        }
        return this.classCode;
    }

    public XDocumentSubstanceMood getMoodCode() {
        return moodCode;
    }

    public void setMoodCode(XDocumentSubstanceMood value) {
        this.moodCode = value;
    }
}
