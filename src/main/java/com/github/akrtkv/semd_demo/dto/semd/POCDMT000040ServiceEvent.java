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
@XmlType(name = "POCD_MT000040.ServiceEvent", propOrder = {
        "code",
        "effectiveTime",
        "serviceForm",
        "serviceType",
        "serviceCond"
})
public class POCDMT000040ServiceEvent {

    @XmlElement(required = true)
    protected Code code;

    @XmlElement(required = true)
    protected EffectiveTime effectiveTime;

    @XmlElement(namespace = "urn:hl7-ru:medService")
    protected ServiceForm serviceForm;

    @XmlElement(namespace = "urn:hl7-ru:medService")
    protected ServiceType serviceType;

    @XmlElement(namespace = "urn:hl7-ru:medService")
    protected ServiceCond serviceCond;

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public EffectiveTime getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(EffectiveTime effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public ServiceForm getServiceForm() {
        return serviceForm;
    }

    public void setServiceForm(ServiceForm serviceForm) {
        this.serviceForm = serviceForm;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public ServiceCond getServiceCond() {
        return serviceCond;
    }

    public void setServiceCond(ServiceCond serviceCond) {
        this.serviceCond = serviceCond;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Code
            extends CENonElements {

    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class EffectiveTime
            extends TS {

    }
}
