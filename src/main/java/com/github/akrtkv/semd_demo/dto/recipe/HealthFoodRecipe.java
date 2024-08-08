package su.medsoft.rir.recipe.dto.rir.recipe;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    @Override
    public String toString() {
        return "HealthFoodRecipe{" +
                "healthFood=" + healthFood +
                '}';
    }
}
