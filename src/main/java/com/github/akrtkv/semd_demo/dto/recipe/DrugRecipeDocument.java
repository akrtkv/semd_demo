package su.medsoft.rir.recipe.dto.rir.recipe;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class DrugRecipeDocument {

    @NotEmpty
    private String misId;

    @NotNull
    @Valid
    private DrugRecipe drugRecipe;

    public String getMisId() {
        return misId;
    }

    public void setMisId(String misId) {
        this.misId = misId;
    }

    public DrugRecipe getDrugRecipe() {
        return drugRecipe;
    }

    public void setDrugRecipe(DrugRecipe drugRecipe) {
        this.drugRecipe = drugRecipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrugRecipeDocument document = (DrugRecipeDocument) o;
        return Objects.equals(drugRecipe, document.drugRecipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(drugRecipe);
    }

    @Override
    public String toString() {
        return "DrugRecipeDocument{" +
                "drugRecipe=" + drugRecipe +
                '}';
    }
}
