//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2022.01.20 at 04:56:34 PM MSK
//

package com.github.akrtkv.semd_demo.dto.semd;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StrucDoc.Table", propOrder = {
        "caption",
        "col",
        "colgroup",
        "thead",
        "tfoot",
        "tbody"
})
public class StrucDocTable {

    protected StrucDocCaption caption;

    protected List<StrucDocCol> col;

    protected List<StrucDocColgroup> colgroup;

    protected StrucDocThead thead;

    protected StrucDocTfoot tfoot;

    @XmlElement(required = true)
    protected List<StrucDocTbody> tbody;

    @XmlAttribute(name = "ID")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    @XmlAttribute(name = "language")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlSchemaType(name = "NMTOKEN")
    protected String language;

    @XmlAttribute(name = "styleCode")
    @XmlSchemaType(name = "NMTOKENS")
    protected List<String> styleCode;

    @XmlAttribute(name = "summary")
    protected String summary;

    @XmlAttribute(name = "width")
    protected String width;

    @XmlAttribute(name = "border")
    protected String border;

    @XmlAttribute(name = "frame")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String frame;

    @XmlAttribute(name = "rules")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String rules;

    @XmlAttribute(name = "cellspacing")
    protected String cellspacing;

    @XmlAttribute(name = "cellpadding")
    protected String cellpadding;

    public StrucDocCaption getCaption() {
        return caption;
    }

    public void setCaption(StrucDocCaption value) {
        this.caption = value;
    }

    public List<StrucDocCol> getCol() {
        if (col == null) {
            col = new ArrayList<StrucDocCol>();
        }
        return this.col;
    }

    public List<StrucDocColgroup> getColgroup() {
        if (colgroup == null) {
            colgroup = new ArrayList<StrucDocColgroup>();
        }
        return this.colgroup;
    }

    public StrucDocThead getThead() {
        return thead;
    }

    public void setThead(StrucDocThead value) {
        this.thead = value;
    }

    public StrucDocTfoot getTfoot() {
        return tfoot;
    }

    public void setTfoot(StrucDocTfoot value) {
        this.tfoot = value;
    }

    public List<StrucDocTbody> getTbody() {
        if (tbody == null) {
            tbody = new ArrayList<StrucDocTbody>();
        }
        return this.tbody;
    }

    public String getID() {
        return id;
    }

    public void setID(String value) {
        this.id = value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String value) {
        this.language = value;
    }

    public List<String> getStyleCode() {
        if (styleCode == null) {
            styleCode = new ArrayList<String>();
        }
        return this.styleCode;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String value) {
        this.summary = value;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String value) {
        this.width = value;
    }

    public String getBorder() {
        return border;
    }

    public void setBorder(String value) {
        this.border = value;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String value) {
        this.frame = value;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String value) {
        this.rules = value;
    }

    public String getCellspacing() {
        return cellspacing;
    }

    public void setCellspacing(String value) {
        this.cellspacing = value;
    }

    public String getCellpadding() {
        return cellpadding;
    }

    public void setCellpadding(String value) {
        this.cellpadding = value;
    }
}
