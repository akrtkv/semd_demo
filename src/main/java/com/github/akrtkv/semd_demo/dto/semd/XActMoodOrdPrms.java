//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2022.01.20 at 04:56:34 PM MSK
//

package com.github.akrtkv.semd_demo.dto.semd;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "x_ActMoodOrdPrms")
@XmlEnum
public enum XActMoodOrdPrms {

    PRMS,
    RQO;

    public static XActMoodOrdPrms fromValue(String v) {
        return valueOf(v);
    }

    public String value() {
        return name();
    }
}
