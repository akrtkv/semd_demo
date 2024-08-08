package su.medsoft.rir.recipe.dto.rir.recipe;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class MedProdRecipeRequest {

    @NotNull
    private List<@Valid MedProdRecipeDocument> medProdRecipeDocuments;

    public List<MedProdRecipeDocument> getMedProdRecipeDocuments() {
        return medProdRecipeDocuments;
    }

    public void setMedProdRecipeDocuments(List<MedProdRecipeDocument> medProdRecipeDocuments) {
        this.medProdRecipeDocuments = medProdRecipeDocuments;
    }

    @Override
    public String toString() {
        return "MedProdRecipeRequest{" +
                "medProdRecipeDocuments=" + medProdRecipeDocuments +
                '}';
    }
}
