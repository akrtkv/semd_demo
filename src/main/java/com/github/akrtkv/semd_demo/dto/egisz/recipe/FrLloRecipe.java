package su.medsoft.rir.recipe.dto.egisz.recipe;

import su.medsoft.rir.recipe.dto.egisz.release.FrLloRelease;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "infoSysCode",
        "documents",
        "releaseDocuments"
})
@XmlRootElement(name = "root")
public class FrLloRecipe {

    @XmlElement(name = "info_sys_code", required = true)
    protected String infoSysCode;

    @XmlElement(name = "documents")
    protected Documents documents;

    @XmlElement(name = "documents")
    protected FrLloRelease.Documents releaseDocuments;

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

    public FrLloRelease.Documents getReleaseDocuments() {
        return releaseDocuments;
    }

    public void setReleaseDocuments(FrLloRelease.Documents releaseDocuments) {
        this.releaseDocuments = releaseDocuments;
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
                "recipe"
        })
        public static class Document {

            @XmlElement(name = "document_id", required = true)
            protected String documentId;

            @XmlElement(name = "doc_date_time", required = true)
            protected String docDateTime;

            protected Citizen citizen;

            @XmlElement(required = true)
            protected Recipe recipe;

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

            public Recipe getRecipe() {
                return recipe;
            }

            public void setRecipe(Recipe value) {
                this.recipe = value;
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
                    "region",
                    "snils",
                    "policySn",
                    "identifyDocs"
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

                @XmlElement(required = true)
                protected String sex;

                protected String region;

                protected String snils;

                @XmlElement(name = "policy_sn")
                protected String policySn;

                @XmlElement(name = "identify_docs")
                protected IdentifyDocs identifyDocs;

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

                public String getRegion() {
                    return region;
                }

                public void setRegion(String value) {
                    this.region = value;
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

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "recipeSerial",
                    "recipeNum",
                    "extRecipeId",
                    "recipeId",
                    "medOrgOid",
                    "seNum",
                    "seName",
                    "doctorName",
                    "staffPositionCode",
                    "doctorSnils",
                    "medicalCard",
                    "benefitCode",
                    "mkb10Code",
                    "drugSmnnCode",
                    "drugName",
                    "medEquipCode",
                    "nutritionCode",
                    "commissionDate",
                    "commissionNum",
                    "payPercent",
                    "qty",
                    "recipeDate",
                    "recipePriorityCode",
                    "recipeExpiryCode",
                    "dateExpiry"
            })
            public static class Recipe {

                @XmlElement(name = "recipe_serial")
                protected String recipeSerial;

                @XmlElement(name = "recipe_num")
                protected String recipeNum;

                @XmlElement(name = "ext_recipe_id", required = true)
                protected String extRecipeId;

                @XmlElement(name = "recipe_id")
                protected String recipeId;

                @XmlElement(name = "med_org_oid")
                protected String medOrgOid;

                @XmlElement(name = "se_num")
                protected String seNum;

                @XmlElement(name = "se_name")
                protected String seName;

                @XmlElement(name = "doctor_name", required = true)
                protected String doctorName;

                @XmlElement(name = "staff_position_code", required = true)
                protected String staffPositionCode;

                @XmlElement(name = "doctor_snils", required = true)
                protected String doctorSnils;

                @XmlElement(name = "medical_card")
                protected String medicalCard;

                @XmlElement(name = "benefit_code", required = true)
                protected String benefitCode;

                @XmlElement(name = "mkb10_code")
                protected String mkb10Code;

                @XmlElement(name = "drug_smnn_code")
                protected String drugSmnnCode;

                @XmlElement(name = "drug_name")
                protected String drugName;

                @XmlElement(name = "med_equip_code")
                protected String medEquipCode;

                @XmlElement(name = "nutrition_code")
                protected String nutritionCode;

                @XmlElement(name = "commission_date")
                protected String commissionDate;

                @XmlElement(name = "commission_num")
                protected String commissionNum;

                @XmlElement(name = "pay_percent")
                protected String payPercent;

                @XmlElement(required = true)
                protected String qty;

                @XmlElement(name = "recipe_date", required = true)
                @XmlSchemaType(name = "date")
                protected XMLGregorianCalendar recipeDate;

                @XmlElement(name = "recipe_priority_code")
                protected String recipePriorityCode;

                @XmlElement(name = "recipe_expiry_code")
                protected String recipeExpiryCode;

                @XmlElement(name = "date_expiry")
                protected String dateExpiry;

                public String getRecipeSerial() {
                    return recipeSerial;
                }

                public void setRecipeSerial(String value) {
                    this.recipeSerial = value;
                }

                public String getRecipeNum() {
                    return recipeNum;
                }

                public void setRecipeNum(String value) {
                    this.recipeNum = value;
                }

                public String getExtRecipeId() {
                    return extRecipeId;
                }

                public void setExtRecipeId(String value) {
                    this.extRecipeId = value;
                }

                public String getRecipeId() {
                    return recipeId;
                }

                public void setRecipeId(String value) {
                    this.recipeId = value;
                }

                public String getMedOrgOid() {
                    return medOrgOid;
                }

                public void setMedOrgOid(String value) {
                    this.medOrgOid = value;
                }

                public String getSeNum() {
                    return seNum;
                }

                public void setSeNum(String value) {
                    this.seNum = value;
                }

                public String getSeName() {
                    return seName;
                }

                public void setSeName(String value) {
                    this.seName = value;
                }

                public String getDoctorName() {
                    return doctorName;
                }

                public void setDoctorName(String value) {
                    this.doctorName = value;
                }

                public String getStaffPositionCode() {
                    return staffPositionCode;
                }

                public void setStaffPositionCode(String value) {
                    this.staffPositionCode = value;
                }

                public String getDoctorSnils() {
                    return doctorSnils;
                }

                public void setDoctorSnils(String value) {
                    this.doctorSnils = value;
                }

                public String getMedicalCard() {
                    return medicalCard;
                }

                public void setMedicalCard(String value) {
                    this.medicalCard = value;
                }

                public String getBenefitCode() {
                    return benefitCode;
                }

                public void setBenefitCode(String value) {
                    this.benefitCode = value;
                }

                public String getMkb10Code() {
                    return mkb10Code;
                }

                public void setMkb10Code(String value) {
                    this.mkb10Code = value;
                }

                public String getDrugSmnnCode() {
                    return drugSmnnCode;
                }

                public void setDrugSmnnCode(String value) {
                    this.drugSmnnCode = value;
                }

                public String getDrugName() {
                    return drugName;
                }

                public void setDrugName(String value) {
                    this.drugName = value;
                }

                public String getMedEquipCode() {
                    return medEquipCode;
                }

                public void setMedEquipCode(String value) {
                    this.medEquipCode = value;
                }

                public String getNutritionCode() {
                    return nutritionCode;
                }

                public void setNutritionCode(String value) {
                    this.nutritionCode = value;
                }

                public String getCommissionDate() {
                    return commissionDate;
                }

                public void setCommissionDate(String value) {
                    this.commissionDate = value;
                }

                public String getCommissionNum() {
                    return commissionNum;
                }

                public void setCommissionNum(String value) {
                    this.commissionNum = value;
                }

                public String getPayPercent() {
                    return payPercent;
                }

                public void setPayPercent(String value) {
                    this.payPercent = value;
                }

                public String getQty() {
                    return qty;
                }

                public void setQty(String value) {
                    this.qty = value;
                }

                public XMLGregorianCalendar getRecipeDate() {
                    return recipeDate;
                }

                public void setRecipeDate(XMLGregorianCalendar value) {
                    this.recipeDate = value;
                }

                public String getRecipePriorityCode() {
                    return recipePriorityCode;
                }

                public void setRecipePriorityCode(String value) {
                    this.recipePriorityCode = value;
                }

                public String getRecipeExpiryCode() {
                    return recipeExpiryCode;
                }

                public void setRecipeExpiryCode(String value) {
                    this.recipeExpiryCode = value;
                }

                public String getDateExpiry() {
                    return dateExpiry;
                }

                public void setDateExpiry(String value) {
                    this.dateExpiry = value;
                }
            }
        }
    }
}
