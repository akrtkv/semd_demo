package su.medsoft.rir.recipe.dto.rir.recipe;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class MedProdRecipeDocument {

    @NotEmpty
    private String misId;

    @NotNull
    @Valid
    private MedicalProductRecipe medicalProductRecipe;

    public String getMisId() {
        return misId;
    }

    public void setMisId(String misId) {
        this.misId = misId;
    }

    public MedicalProductRecipe getMedicalProductRecipe() {
        return medicalProductRecipe;
    }

    public void setMedicalProductRecipe(MedicalProductRecipe medicalProductRecipe) {
        this.medicalProductRecipe = medicalProductRecipe;
    }

    @Override
    public String toString() {
        return "MedProdRecipeDocument{" +
                "medicalProductRecipe=" + medicalProductRecipe +
                '}';
    }
}
