//
// This file was generated by the Eclipse Implementation of JAXB, v2.3.3 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2023.05.24 at 10:11:26 PM MSK 
//

package com.github.akrtkv.semd_demo.dto.semd;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "POCD_MT000040.DocumentationOf", propOrder = {
        "serviceEvent"
})
public class POCDMT000040DocumentationOf {

    @XmlElement(required = true)
    protected POCDMT000040ServiceEvent serviceEvent;

    public POCDMT000040ServiceEvent getServiceEvent() {
        return serviceEvent;
    }

    public void setServiceEvent(POCDMT000040ServiceEvent value) {
        this.serviceEvent = value;
    }
}
