//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2022.01.20 at 04:56:34 PM MSK
//

package com.github.akrtkv.semd_demo.dto.semd;

import com.github.akrtkv.semd_demo.dto.semd.identity.POCDMT000040DocInfo;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ST_ST", propOrder = {
        "content"
})
@XmlSeeAlso({
        POCDMT000040DocInfo.Series.class,
        POCDMT000040DocInfo.Number.class
})
public class STST {

    @XmlValue
    protected String content;

    public String getContent() {
        return content;
    }

    public void setContent(String value) {
        this.content = value;
    }
}
