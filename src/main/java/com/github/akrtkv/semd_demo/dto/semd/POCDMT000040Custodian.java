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

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "POCD_MT000040.Custodian", propOrder = {
        "assignedCustodian"
})
public class POCDMT000040Custodian {

    @XmlElement(required = true)
    protected POCDMT000040AssignedCustodian assignedCustodian;

    public POCDMT000040AssignedCustodian getAssignedCustodian() {
        return assignedCustodian;
    }

    public void setAssignedCustodian(POCDMT000040AssignedCustodian value) {
        this.assignedCustodian = value;
    }
}
