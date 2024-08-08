package su.medsoft.rir.recipe.dto.egisz.callback;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Element {

    @XmlElement(name = "document_id")
    private String documentId;

    @XmlElement
    private String status;

    @XmlElement(name = "reg_num")
    private String registryNumber;

    @XmlElement(name = "recipe_id")
    private String recipeId;

    @XmlElement(name = "relise_id")
    private String releaseId;

    @XmlElement
    private Errors errors;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegistryNumber() {
        return registryNumber;
    }

    public void setRegistryNumber(String registryNumber) {
        this.registryNumber = registryNumber;
    }

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public String getReleaseId() {
        return releaseId;
    }

    public void setReleaseId(String releaseId) {
        this.releaseId = releaseId;
    }

    public Errors getErrors() {
        return errors;
    }

    public void setErrors(Errors errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "Element{" +
                "documentId='" + documentId + '\'' +
                ", status='" + status + '\'' +
                ", registryNumber='" + registryNumber + '\'' +
                ", recipeId='" + recipeId + '\'' +
                ", releaseId='" + releaseId + '\'' +
                ", errors=" + errors +
                '}';
    }
}
