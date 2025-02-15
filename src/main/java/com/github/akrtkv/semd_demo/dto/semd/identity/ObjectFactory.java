//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2022.01.20 at 04:56:34 PM MSK
//

package com.github.akrtkv.semd_demo.dto.semd.identity;

import com.github.akrtkv.semd_demo.dto.semd.ST;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

@XmlRegistry
public class ObjectFactory {

    private final static QName _Patronymic_QNAME = new QName("urn:hl7-ru:identity", "Patronymic");

    private final static QName _IdentityDoc_QNAME = new QName("urn:hl7-ru:identity", "IdentityDoc");

    private final static QName _InsurancePolicy_QNAME = new QName("urn:hl7-ru:identity", "InsurancePolicy");

    private final static QName _Props_QNAME = new QName("urn:hl7-ru:identity", "Props");

    private final static QName _DocInfo_QNAME = new QName("urn:hl7-ru:identity", "DocInfo");

    public ObjectFactory() {
    }

    public EffectiveTime createEffectiveTime() {
        return new EffectiveTime();
    }

    public POCDMT000040DocInfo createPOCDMT000040DocInfo() {
        return new POCDMT000040DocInfo();
    }

    public POCDMT000040InsurancePolicy createPOCDMT000040InsurancePolicy() {
        return new POCDMT000040InsurancePolicy();
    }

    public POCDMT000040IdentityDoc createPOCDMT000040IdentityDoc() {
        return new POCDMT000040IdentityDoc();
    }

    public POCDMT000040Props createPOCDMT000040Props() {
        return new POCDMT000040Props();
    }

    public EffectiveTime.Low createEffectiveTimeLow() {
        return new EffectiveTime.Low();
    }

    public EffectiveTime.High createEffectiveTimeHigh() {
        return new EffectiveTime.High();
    }

    public POCDMT000040DocInfo.IdentityDocType createPOCDMT000040DocInfoIdentityDocType() {
        return new POCDMT000040DocInfo.IdentityDocType();
    }

    public POCDMT000040DocInfo.InsurancePolicyType createPOCDMT000040DocInfoInsurancePolicyType() {
        return new POCDMT000040DocInfo.InsurancePolicyType();
    }

    public POCDMT000040DocInfo.Series createPOCDMT000040DocInfoSeries() {
        return new POCDMT000040DocInfo.Series();
    }

    public POCDMT000040DocInfo.Number createPOCDMT000040DocInfoNumber() {
        return new POCDMT000040DocInfo.Number();
    }

    public POCDMT000040InsurancePolicy.InsurancePolicyType createPOCDMT000040InsurancePolicyInsurancePolicyType() {
        return new POCDMT000040InsurancePolicy.InsurancePolicyType();
    }

    public POCDMT000040IdentityDoc.IdentityCardType createPOCDMT000040IdentityDocIdentityCardType() {
        return new POCDMT000040IdentityDoc.IdentityCardType();
    }

    public POCDMT000040IdentityDoc.IssueDate createPOCDMT000040IdentityDocIssueDate() {
        return new POCDMT000040IdentityDoc.IssueDate();
    }

    @XmlElementDecl(namespace = "urn:hl7-ru:identity", name = "Patronymic")
    public JAXBElement<ST> createPatronymic(ST value) {
        return new JAXBElement<ST>(_Patronymic_QNAME, ST.class, null, value);
    }

    @XmlElementDecl(namespace = "urn:hl7-ru:identity", name = "IdentityDoc")
    public JAXBElement<POCDMT000040IdentityDoc> createIdentityDoc(POCDMT000040IdentityDoc value) {
        return new JAXBElement<POCDMT000040IdentityDoc>(_IdentityDoc_QNAME, POCDMT000040IdentityDoc.class, null, value);
    }

    @XmlElementDecl(namespace = "urn:hl7-ru:identity", name = "InsurancePolicy")
    public JAXBElement<POCDMT000040InsurancePolicy> createInsurancePolicy(POCDMT000040InsurancePolicy value) {
        return new JAXBElement<POCDMT000040InsurancePolicy>(_InsurancePolicy_QNAME, POCDMT000040InsurancePolicy.class, null, value);
    }

    @XmlElementDecl(namespace = "urn:hl7-ru:identity", name = "Props")
    public JAXBElement<POCDMT000040Props> createProps(POCDMT000040Props value) {
        return new JAXBElement<POCDMT000040Props>(_Props_QNAME, POCDMT000040Props.class, null, value);
    }

    @XmlElementDecl(namespace = "urn:hl7-ru:identity", name = "DocInfo")
    public JAXBElement<POCDMT000040DocInfo> createDocInfo(POCDMT000040DocInfo value) {
        return new JAXBElement<POCDMT000040DocInfo>(_DocInfo_QNAME, POCDMT000040DocInfo.class, null, value);
    }
}
