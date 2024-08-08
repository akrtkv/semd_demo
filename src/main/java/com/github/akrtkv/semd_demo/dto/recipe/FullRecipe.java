package su.medsoft.rir.recipe.dto.rir.recipe;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class FullRecipe extends Recipe {

    @Schema(description = "Для льготного рецепта")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Drug drug;

    @Schema(description = "Для обычного рецепта")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Drug> drugs;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private HealthFood healthFood;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MedicalProduct medicalProduct;

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public List<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs = drugs;
    }

    public HealthFood getHealthFood() {
        return healthFood;
    }

    public void setHealthFood(HealthFood healthFood) {
        this.healthFood = healthFood;
    }

    public MedicalProduct getMedicalProduct() {
        return medicalProduct;
    }

    public void setMedicalProduct(MedicalProduct medicalProduct) {
        this.medicalProduct = medicalProduct;
    }

    @Override
    public String toString() {
        return "FullRecipe{" +
                "drug=" + drug +
                ", drugs=" + drugs +
                ", healthFood=" + healthFood +
                ", medicalProduct=" + medicalProduct +
                '}';
    }
}
