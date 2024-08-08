package com.github.akrtkv.semd_demo.dto.recipe;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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
}
