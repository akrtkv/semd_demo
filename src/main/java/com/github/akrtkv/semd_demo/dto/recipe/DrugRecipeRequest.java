package su.medsoft.rir.recipe.dto.rir.recipe;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

public class DrugRecipeRequest {

    @NotNull
    private List<@Valid DrugRecipeDocument> drugRecipeDocuments;

    public List<DrugRecipeDocument> getDrugRecipeDocuments() {
        return drugRecipeDocuments;
    }

    public void setDrugRecipeDocuments(List<DrugRecipeDocument> drugRecipeDocuments) {
        this.drugRecipeDocuments = drugRecipeDocuments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DrugRecipeRequest that = (DrugRecipeRequest) o;
        return Objects.equals(drugRecipeDocuments, that.drugRecipeDocuments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(drugRecipeDocuments);
    }

    @Override
    public String toString() {
        return "DrugRecipeRequest{" +
                "drugRecipeDocuments=" + drugRecipeDocuments +
                '}';
    }
}
