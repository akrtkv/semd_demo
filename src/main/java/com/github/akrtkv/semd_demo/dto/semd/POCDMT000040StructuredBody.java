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
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "POCD_MT000040.StructuredBody", propOrder = {
        "component"
})
public class POCDMT000040StructuredBody {

    @XmlElement(required = true)
    protected List<POCDMT000040Component3> component;

    public List<POCDMT000040Component3> getComponent() {
        if (component == null) {
            component = new ArrayList<POCDMT000040Component3>();
        }
        return this.component;
    }
}
