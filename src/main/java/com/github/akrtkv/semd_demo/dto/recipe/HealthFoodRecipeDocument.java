package com.github.akrtkv.semd_demo.dto.recipe;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

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
}
