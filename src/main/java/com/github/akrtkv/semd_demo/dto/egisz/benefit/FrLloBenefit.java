package su.medsoft.rir.recipe.dto.egisz.benefit;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "infoSysCode",
        "documents"
})
@XmlRootElement(name = "root")
public class FrLloBenefit {

    @XmlElement(name = "info_sys_code", required = true)
    protected String infoSysCode;

    @XmlElement(required = true)
    protected Documents documents;

    public String getInfoSysCode() {
        return infoSysCode;
    }

    public void setInfoSysCode(String value) {
        this.infoSysCode = value;
    }

    public Documents getDocuments() {
        return documents;
    }

    public void setDocuments(Documents value) {
        this.documents = value;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
            "document"
    })
    public static class Documents {

        @XmlElement(required = true)
        protected List<Document> document;

        public List<Document> getDocument() {
            if (document == null) {
                document = new ArrayList<Document>();
            }
            return this.document;
        }

        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
                "documentId",
                "docDateTime",
                "citizen",
                "benefits"
        })
        public static class Document {

            @XmlElement(name = "document_id", required = true)
            protected String documentId;

            @XmlElement(name = "doc_date_time", required = true)
            protected String docDateTime;

            @XmlElement(required = true)
            protected Citizen citizen;

            @XmlElement(required = true)
            protected Benefits benefits;

            public String getDocumentId() {
                return documentId;
            }

            public void setDocumentId(String value) {
                this.documentId = value;
            }

            public String getDocDateTime() {
                return docDateTime;
            }

            public void setDocDateTime(String value) {
                this.docDateTime = value;
            }

            public Citizen getCitizen() {
                return citizen;
            }

            public void setCitizen(Citizen value) {
                this.citizen = value;
            }

            public Benefits getBenefits() {
                return benefits;
            }

            public void setBenefits(Benefits value) {
                this.benefits = value;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "benefit"
            })
            public static class Benefits {

                @XmlElement(required = true)
                protected List<Benefit> benefit;

                public List<Benefit> getBenefit() {
                    if (benefit == null) {
                        benefit = new ArrayList<Benefit>();
                    }
                    return this.benefit;
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "benefitCode",
                        "extBenefitCode",
                        "diagnosis",
                        "receiveDate",
                        "cancelDate"
                })
                public static class Benefit {

                    @XmlElement(name = "benefit_code")
                    protected String benefitCode;

                    @XmlElement(name = "ext_benefit_code")
                    protected String extBenefitCode;

                    protected String diagnosis;

                    @XmlElement(name = "receive_date", required = true)
                    @XmlSchemaType(name = "date")
                    protected XMLGregorianCalendar receiveDate;

                    @XmlElement(name = "cancel_date")
                    @XmlSchemaType(name = "date")
                    protected XMLGregorianCalendar cancelDate;

                    public String getBenefitCode() {
                        return benefitCode;
                    }

                    public void setBenefitCode(String value) {
                        this.benefitCode = value;
                    }

                    public String getExtBenefitCode() {
                        return extBenefitCode;
                    }

                    public void setExtBenefitCode(String value) {
                        this.extBenefitCode = value;
                    }

                    public String getDiagnosis() {
                        return diagnosis;
                    }

                    public void setDiagnosis(String value) {
                        this.diagnosis = value;
                    }

                    public XMLGregorianCalendar getReceiveDate() {
                        return receiveDate;
                    }

                    public void setReceiveDate(XMLGregorianCalendar value) {
                        this.receiveDate = value;
                    }

                    public XMLGregorianCalendar getCancelDate() {
                        return cancelDate;
                    }

                    public void setCancelDate(XMLGregorianCalendar value) {
                        this.cancelDate = value;
                    }
                }
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "registerId",
                    "extCitizenId",
                    "name",
                    "surname",
                    "patronymic",
                    "firstsurname",
                    "birthdate",
                    "sex",
                    "citizenship",
                    "citizenshipConfirm",
                    "snils",
                    "policySn",
                    "identifyDocs",
                    "region",
                    "addresses"
            })
            public static class Citizen {

                @XmlElement(name = "register_id")
                protected String registerId;

                @XmlElement(name = "ext_citizen_id")
                protected String extCitizenId;

                protected String name;

                protected String surname;

                protected String patronymic;

                protected String firstsurname;

                @XmlSchemaType(name = "date")
                protected XMLGregorianCalendar birthdate;

                protected String sex;

                @XmlElement(required = true)
                protected Object citizenship;

                @XmlElement(name = "citizenship_confirm")
                protected CitizenshipConfirm citizenshipConfirm;

                protected String snils;

                @XmlElement(name = "policy_sn")
                protected String policySn;

                @XmlElement(name = "identify_docs")
                protected IdentifyDocs identifyDocs;

                @XmlElement(required = true)
                protected String region;

                protected Addresses addresses;

                public String getRegisterId() {
                    return registerId;
                }

                public void setRegisterId(String value) {
                    this.registerId = value;
                }

                public String getExtCitizenId() {
                    return extCitizenId;
                }

                public void setExtCitizenId(String value) {
                    this.extCitizenId = value;
                }

                public String getName() {
                    return name;
                }

                public void setName(String value) {
                    this.name = value;
                }

                public String getSurname() {
                    return surname;
                }

                public void setSurname(String value) {
                    this.surname = value;
                }

                public String getPatronymic() {
                    return patronymic;
                }

                public void setPatronymic(String value) {
                    this.patronymic = value;
                }

                public String getFirstsurname() {
                    return firstsurname;
                }

                public void setFirstsurname(String value) {
                    this.firstsurname = value;
                }

                public XMLGregorianCalendar getBirthdate() {
                    return birthdate;
                }

                public void setBirthdate(XMLGregorianCalendar value) {
                    this.birthdate = value;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String value) {
                    this.sex = value;
                }

                public Object getCitizenship() {
                    return citizenship;
                }

                public void setCitizenship(Object value) {
                    this.citizenship = value;
                }

                public CitizenshipConfirm getCitizenshipConfirm() {
                    return citizenshipConfirm;
                }

                public void setCitizenshipConfirm(CitizenshipConfirm value) {
                    this.citizenshipConfirm = value;
                }

                public String getSnils() {
                    return snils;
                }

                public void setSnils(String value) {
                    this.snils = value;
                }

                public String getPolicySn() {
                    return policySn;
                }

                public void setPolicySn(String value) {
                    this.policySn = value;
                }

                public IdentifyDocs getIdentifyDocs() {
                    return identifyDocs;
                }

                public void setIdentifyDocs(IdentifyDocs value) {
                    this.identifyDocs = value;
                }

                public String getRegion() {
                    return region;
                }

                public void setRegion(String value) {
                    this.region = value;
                }

                public Addresses getAddresses() {
                    return addresses;
                }

                public void setAddresses(Addresses value) {
                    this.addresses = value;
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "address"
                })
                public static class Addresses {

                    @XmlElement(required = true)
                    protected List<Address> address;

                    public List<Address> getAddress() {
                        if (address == null) {
                            address = new ArrayList<Address>();
                        }
                        return this.address;
                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                            "addressTypeCode",
                            "fiasAddress",
                            "fiasHome",
                            "region",
                            "area",
                            "prefixArea",
                            "street",
                            "prefixStreet",
                            "houseNum",
                            "buildNum",
                            "strucNum",
                            "roomNum",
                            "liveStartDate",
                            "liveEndDate"
                    })
                    public static class Address {

                        @XmlElement(name = "address_type_code", required = true)
                        protected String addressTypeCode;

                        @XmlElement(name = "fias_address")
                        protected String fiasAddress;

                        @XmlElement(name = "fias_home")
                        protected String fiasHome;

                        protected String region;

                        protected String area;

                        @XmlElement(name = "prefix_area")
                        protected String prefixArea;

                        protected String street;

                        @XmlElement(name = "prefix_street")
                        protected String prefixStreet;

                        @XmlElement(name = "house_num")
                        protected String houseNum;

                        @XmlElement(name = "build_num")
                        protected String buildNum;

                        @XmlElement(name = "struc_num")
                        protected String strucNum;

                        @XmlElement(name = "room_num")
                        protected String roomNum;

                        @XmlElement(name = "live_start_date", required = true)
                        @XmlSchemaType(name = "date")
                        protected XMLGregorianCalendar liveStartDate;

                        @XmlElement(name = "live_end_date")
                        @XmlSchemaType(name = "date")
                        protected XMLGregorianCalendar liveEndDate;

                        public String getAddressTypeCode() {
                            return addressTypeCode;
                        }

                        public void setAddressTypeCode(String value) {
                            this.addressTypeCode = value;
                        }

                        public String getFiasAddress() {
                            return fiasAddress;
                        }

                        public void setFiasAddress(String value) {
                            this.fiasAddress = value;
                        }

                        public String getFiasHome() {
                            return fiasHome;
                        }

                        public void setFiasHome(String value) {
                            this.fiasHome = value;
                        }

                        public String getRegion() {
                            return region;
                        }

                        public void setRegion(String value) {
                            this.region = value;
                        }

                        public String getArea() {
                            return area;
                        }

                        public void setArea(String value) {
                            this.area = value;
                        }

                        public String getPrefixArea() {
                            return prefixArea;
                        }

                        public void setPrefixArea(String value) {
                            this.prefixArea = value;
                        }

                        public String getStreet() {
                            return street;
                        }

                        public void setStreet(String value) {
                            this.street = value;
                        }

                        public String getPrefixStreet() {
                            return prefixStreet;
                        }

                        public void setPrefixStreet(String value) {
                            this.prefixStreet = value;
                        }

                        public String getHouseNum() {
                            return houseNum;
                        }

                        public void setHouseNum(String value) {
                            this.houseNum = value;
                        }

                        public String getBuildNum() {
                            return buildNum;
                        }

                        public void setBuildNum(String value) {
                            this.buildNum = value;
                        }

                        public String getStrucNum() {
                            return strucNum;
                        }

                        public void setStrucNum(String value) {
                            this.strucNum = value;
                        }

                        public String getRoomNum() {
                            return roomNum;
                        }

                        public void setRoomNum(String value) {
                            this.roomNum = value;
                        }

                        public XMLGregorianCalendar getLiveStartDate() {
                            return liveStartDate;
                        }

                        public void setLiveStartDate(XMLGregorianCalendar value) {
                            this.liveStartDate = value;
                        }

                        public XMLGregorianCalendar getLiveEndDate() {
                            return liveEndDate;
                        }

                        public void setLiveEndDate(XMLGregorianCalendar value) {
                            this.liveEndDate = value;
                        }
                    }
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "doc"
                })
                public static class CitizenshipConfirm {

                    protected Doc doc;

                    public Doc getDoc() {
                        return doc;
                    }

                    public void setDoc(Doc value) {
                        this.doc = value;
                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                            "docType",
                            "docTypeName",
                            "serial",
                            "num",
                            "dateIssue",
                            "dateExpiry",
                            "authority"
                    })
                    public static class Doc {

                        @XmlElement(name = "doc_type", required = true)
                        protected String docType;

                        @XmlElement(name = "doc_type_name")
                        protected String docTypeName;

                        protected String serial;

                        @XmlElement(required = true)
                        protected String num;

                        @XmlElement(name = "date_issue", required = true)
                        @XmlSchemaType(name = "date")
                        protected XMLGregorianCalendar dateIssue;

                        @XmlElement(name = "date_expiry")
                        @XmlSchemaType(name = "date")
                        protected XMLGregorianCalendar dateExpiry;

                        protected String authority;

                        public String getDocType() {
                            return docType;
                        }

                        public void setDocType(String value) {
                            this.docType = value;
                        }

                        public String getDocTypeName() {
                            return docTypeName;
                        }

                        public void setDocTypeName(String value) {
                            this.docTypeName = value;
                        }

                        public String getSerial() {
                            return serial;
                        }

                        public void setSerial(String value) {
                            this.serial = value;
                        }

                        public String getNum() {
                            return num;
                        }

                        public void setNum(String value) {
                            this.num = value;
                        }

                        public XMLGregorianCalendar getDateIssue() {
                            return dateIssue;
                        }

                        public void setDateIssue(XMLGregorianCalendar value) {
                            this.dateIssue = value;
                        }

                        public XMLGregorianCalendar getDateExpiry() {
                            return dateExpiry;
                        }

                        public void setDateExpiry(XMLGregorianCalendar value) {
                            this.dateExpiry = value;
                        }

                        public String getAuthority() {
                            return authority;
                        }

                        public void setAuthority(String value) {
                            this.authority = value;
                        }
                    }
                }

                @XmlAccessorType(XmlAccessType.FIELD)
                @XmlType(name = "", propOrder = {
                        "doc"
                })
                public static class IdentifyDocs {

                    @XmlElement(required = true)
                    protected List<Doc> doc;

                    public List<Doc> getDoc() {
                        if (doc == null) {
                            doc = new ArrayList<Doc>();
                        }
                        return this.doc;
                    }

                    @XmlAccessorType(XmlAccessType.FIELD)
                    @XmlType(name = "", propOrder = {
                            "docType",
                            "serial",
                            "num",
                            "dateIssue",
                            "authority",
                            "dateExpiry"
                    })
                    public static class Doc {

                        @XmlElement(name = "doc_type", required = true)
                        protected String docType;

                        protected String serial;

                        @XmlElement(required = true)
                        protected String num;

                        @XmlElement(name = "date_issue", required = true)
                        @XmlSchemaType(name = "date")
                        protected XMLGregorianCalendar dateIssue;

                        protected String authority;

                        @XmlElement(name = "date_expiry")
                        @XmlSchemaType(name = "date")
                        protected XMLGregorianCalendar dateExpiry;

                        public String getDocType() {
                            return docType;
                        }

                        public void setDocType(String value) {
                            this.docType = value;
                        }

                        public String getSerial() {
                            return serial;
                        }

                        public void setSerial(String value) {
                            this.serial = value;
                        }

                        public String getNum() {
                            return num;
                        }

                        public void setNum(String value) {
                            this.num = value;
                        }

                        public XMLGregorianCalendar getDateIssue() {
                            return dateIssue;
                        }

                        public void setDateIssue(XMLGregorianCalendar value) {
                            this.dateIssue = value;
                        }

                        public String getAuthority() {
                            return authority;
                        }

                        public void setAuthority(String value) {
                            this.authority = value;
                        }

                        public XMLGregorianCalendar getDateExpiry() {
                            return dateExpiry;
                        }

                        public void setDateExpiry(XMLGregorianCalendar value) {
                            this.dateExpiry = value;
                        }
                    }
                }
            }
        }
    }
}
