//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2022.01.20 at 04:56:34 PM MSK
//

package com.github.akrtkv.semd_demo.dto.semd;

import su.medsoft.rir.recipe.dto.iemk_new_edition.fias.FiasAddress;
import su.medsoft.rir.recipe.dto.iemk_new_edition.identity.POCDMT000040Props;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "POCD_MT000040.Organization_Patient", propOrder = {
        "id",
        "props",
        "name",
        "telecom",
        "addr"
})
public class POCDMT000040OrganizationPatient {

    protected List<Id> id;

    @XmlElement(name = "Props", namespace = "urn:hl7-ru:identity")
    protected POCDMT000040Props props;

    protected String name;

    protected List<TEL> telecom;

    protected Addr addr;

    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;

    public List<Id> getId() {
        if (id == null) {
            id = new ArrayList<Id>();
        }
        return this.id;
    }

    public POCDMT000040Props getProps() {
        return props;
    }

    public void setProps(POCDMT000040Props value) {
        this.props = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public List<TEL> getTelecom() {
        if (telecom == null) {
            telecom = new ArrayList<TEL>();
        }
        return this.telecom;
    }

    public Addr getAddr() {
        return addr;
    }

    public void setAddr(Addr value) {
        this.addr = value;
    }

    public List<String> getNullFlavor() {
        if (nullFlavor == null) {
            nullFlavor = new ArrayList<String>();
        }
        return this.nullFlavor;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "streetAddressLine",
            "stateCode",
            "postalCode",
            "address"
    })
    public static class Addr {

        protected String streetAddressLine;

        @XmlElement(namespace = "urn:hl7-ru:address")
        protected CDAddressStateCode stateCode;

        protected AdxpPostalCode postalCode;

        @XmlElement(name = "Address", namespace = "urn:hl7-ru:fias")
        protected FiasAddress address;

        @XmlAttribute(name = "use")
        protected List<String> use;

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
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Id
            extends II {

    }
}
