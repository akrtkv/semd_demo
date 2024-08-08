package com.github.akrtkv.semd_demo.dto.recipe;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class HealthFoodRecipe extends Recipe {

    @Schema(description = "Лечебное питание")
    @NotNull
    @Valid
    private HealthFood healthFood;

    public HealthFood getHealthFood() {
        return healthFood;
    }

    public void setHealthFood(HealthFood healthFood) {
        this.healthFood = healthFood;
    }
}
