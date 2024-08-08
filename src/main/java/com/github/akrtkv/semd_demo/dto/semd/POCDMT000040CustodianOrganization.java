//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2022.01.20 at 04:56:34 PM MSK
//

package com.github.akrtkv.semd_demo.dto.semd;

import com.github.akrtkv.semd_demo.dto.semd.fias.FiasAddress;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "POCD_MT000040.CustodianOrganization", propOrder = {
        "id",
        "name",
        "telecom",
        "addr"
})
public class POCDMT000040CustodianOrganization {

    @XmlElement(required = true)
    protected Id id;

    @XmlElement(required = true)
    protected String name;

    protected TEL telecom;

    @XmlElement(required = true)
    protected Addr addr;

    @XmlAttribute(name = "classCode", required = true)
    protected String classCode;

    public Id getId() {
        return id;
    }

    public void setId(Id value) {
        this.id = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public TEL getTelecom() {
        return telecom;
    }

    public void setTelecom(TEL value) {
        this.telecom = value;
    }

    public Addr getAddr() {
        return addr;
    }

    public void setAddr(Addr value) {
        this.addr = value;
    }

    public String getClassCode() {
        if (classCode == null) {
            return "ORG";
        } else {
            return classCode;
        }
    }

    public void setClassCode(String value) {
        this.classCode = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "streetAddressLine",
            "stateCode",
            "postalCode",
            "address"
    })
    public static class Addr {

        @XmlElement(required = true)
        protected String streetAddressLine;

        @XmlElement(namespace = "urn:hl7-ru:address", required = true)
        protected CDAddressStateCode stateCode;

        @XmlElement(required = true)
        protected AdxpPostalCode postalCode;

        @XmlElement(name = "Address", namespace = "urn:hl7-ru:fias", required = true)
        protected FiasAddress address;

        @XmlAttribute(name = "use")
        protected List<String> use;

        @XmlAttribute(name = "nullFlavor")
        protected List<String> nullFlavor;

        public String getStreetAddressLine() {
            return streetAddressLine;
        }

        public void setStreetAddressLine(String value) {
            this.streetAddressLine = value;
        }

        public CDAddressStateCode getStateCode() {
            return stateCode;
        }

        public void setStateCode(CDAddressStateCode value) {
            this.stateCode = value;
        }

        public AdxpPostalCode getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(AdxpPostalCode value) {
            this.postalCode = value;
        }

        public FiasAddress getAddress() {
            return address;
        }

        public void setAddress(FiasAddress value) {
            this.address = value;
        }

        public List<String> getUse() {
            if (use == null) {
                use = new ArrayList<String>();
            }
            return this.use;
        }

        public List<String> getNullFlavor() {
            if (nullFlavor == null) {
                nullFlavor = new ArrayList<String>();
            }
            return this.nullFlavor;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Id
            extends II {

    }
}
