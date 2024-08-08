package su.medsoft.rir.recipe.dto.rir.recipe;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class HealthFoodRecipeDocument {

    @NotEmpty
    private String misId;

    @NotNull
    @Valid
    private HealthFoodRecipe healthFoodRecipe;

    public String getMisId() {
        return misId;
    }

    public void setMisId(String misId) {
        this.misId = misId;
    }

    public HealthFoodRecipe getHealthFoodRecipe() {
        return healthFoodRecipe;
    }

    public void setHealthFoodRecipe(HealthFoodRecipe healthFoodRecipe) {
        this.healthFoodRecipe = healthFoodRecipe;
    }

    @Override
    public String toString() {
        return "HealthFoodRecipeDocument{" +
                "healthFoodRecipe=" + healthFoodRecipe +
                '}';
    }
}
