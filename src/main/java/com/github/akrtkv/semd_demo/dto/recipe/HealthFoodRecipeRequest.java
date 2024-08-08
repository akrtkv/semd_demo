package su.medsoft.rir.recipe.dto.rir.recipe;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

public class HealthFoodRecipeRequest {

    @NotNull
    private List<@Valid HealthFoodRecipeDocument> healthFoodRecipeDocuments;

    public List<HealthFoodRecipeDocument> getHealthFoodRecipeDocuments() {
        return healthFoodRecipeDocuments;
    }

    public void setHealthFoodRecipeDocuments(List<HealthFoodRecipeDocument> healthFoodRecipeDocuments) {
        this.healthFoodRecipeDocuments = healthFoodRecipeDocuments;
    }

    @Override
    public String toString() {
        return "HealthFoodRecipeRequest{" +
                "healthFoodRecipeDocuments=" + healthFoodRecipeDocuments +
                '}';
    }
}
