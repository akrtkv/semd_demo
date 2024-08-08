package su.medsoft.rir.recipe.dto.egisz.release;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "infoSysCode",
        "documents"
})
@XmlRootElement(name = "root")
public class FrLloRelease {

    @XmlElement(name = "info_sys_code")
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
                "relise"
        })
        public static class Document {

            @XmlElement(name = "document_id", required = true)
            protected String documentId;

            @XmlElement(name = "doc_date_time", required = true)
            protected String docDateTime;

            @XmlElement(required = true)
            protected Relise relise;

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

            public Relise getRelise() {
                return relise;
            }

            public void setRelise(Relise value) {
                this.relise = value;
            }

            @XmlAccessorType(XmlAccessType.FIELD)
            @XmlType(name = "", propOrder = {
                    "reliseId",
                    "extReliseId",
                    "recipeId",
                    "extRecipeId",
                    "medOrgOid",
                    "seNum",
                    "seName",
                    "staffName",
                    "staffPositionCode",
                    "staffSnils",
                    "drugKlpCode",
                    "medEquipCode",
                    "nutritionCode",
                    "serviceTypesCode",
                    "refuseReasonCode",
                    "packQty",
                    "itemQty",
                    "price",
                    "releaseDate"
            })
            public static class Relise {

                @XmlElement(name = "relise_id")
                protected String reliseId;

                @XmlElement(name = "ext_relise_id")
                protected String extReliseId;

                @XmlElement(name = "recipe_id")
                protected String recipeId;

                @XmlElement(name = "ext_recipe_id")
                protected String extRecipeId;

                @XmlElement(name = "med_org_oid")
                protected String medOrgOid;

                @XmlElement(name = "se_num")
                protected String seNum;

                @XmlElement(name = "se_name")
                protected String seName;

                @XmlElement(name = "staff_name", required = true)
                protected String staffName;

                @XmlElement(name = "staff_position_code")
                protected String staffPositionCode;

                @XmlElement(name = "staff_snils")
                protected String staffSnils;

                @XmlElement(name = "drug_klp_code")
                protected String drugKlpCode;

                @XmlElement(name = "med_equip_code")
                protected String medEquipCode;

                @XmlElement(name = "nutrition_code")
                protected String nutritionCode;

                @XmlElement(name = "service_types_code")
                protected String serviceTypesCode;

                @XmlElement(name = "refuse_reason_code")
                protected String refuseReasonCode;

                @XmlElement(name = "pack_qty")
                protected Integer packQty;

                @XmlElement(name = "item_qty")
                protected Integer itemQty;

                protected BigDecimal price;

                @XmlElement(name = "release_date", required = true)
                @XmlSchemaType(name = "date")
                protected XMLGregorianCalendar releaseDate;

                public String getReliseId() {
                    return reliseId;
                }

                public void setReliseId(String value) {
                    this.reliseId = value;
                }

                public String getExtReliseId() {
                    return extReliseId;
                }

                public void setExtReliseId(String value) {
                    this.extReliseId = value;
                }

                public String getRecipeId() {
                    return recipeId;
                }

                public void setRecipeId(String value) {
                    this.recipeId = value;
                }

                public String getExtRecipeId() {
                    return extRecipeId;
                }

                public void setExtRecipeId(String value) {
                    this.extRecipeId = value;
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

                public String getStaffName() {
                    return staffName;
                }

                public void setStaffName(String value) {
                    this.staffName = value;
                }

                public String getStaffPositionCode() {
                    return staffPositionCode;
                }

                public void setStaffPositionCode(String value) {
                    this.staffPositionCode = value;
                }

                public String getStaffSnils() {
                    return staffSnils;
                }

                public void setStaffSnils(String value) {
                    this.staffSnils = value;
                }

                public String getDrugKlpCode() {
                    return drugKlpCode;
                }

                public void setDrugKlpCode(String value) {
                    this.drugKlpCode = value;
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

                public String getServiceTypesCode() {
                    return serviceTypesCode;
                }

                public void setServiceTypesCode(String value) {
                    this.serviceTypesCode = value;
                }

                public String getRefuseReasonCode() {
                    return refuseReasonCode;
                }

                public void setRefuseReasonCode(String value) {
                    this.refuseReasonCode = value;
                }

                public Integer getPackQty() {
                    return packQty;
                }

                public void setPackQty(Integer value) {
                    this.packQty = value;
                }

                public Integer getItemQty() {
                    return itemQty;
                }

                public void setItemQty(Integer value) {
                    this.itemQty = value;
                }

                public BigDecimal getPrice() {
                    return price;
                }

                public void setPrice(BigDecimal value) {
                    this.price = value;
                }

                public XMLGregorianCalendar getReleaseDate() {
                    return releaseDate;
                }

                public void setReleaseDate(XMLGregorianCalendar value) {
                    this.releaseDate = value;
                }
            }
        }
    }
}
