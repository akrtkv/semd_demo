package com.github.akrtkv.semd_demo.ctrl;

import com.github.akrtkv.semd_demo.dto.recipe.FullRecipe;
import com.github.akrtkv.semd_demo.service.SemdService;
import com.github.akrtkv.semd_demo.util.ApiPaths;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class SemdCtrl {

    private final SemdService semdService;

    public SemdCtrl(SemdService semdService) {
        this.semdService = semdService;
    }

    @Operation(summary = "Льготный рецепт на лекарственный препарат")
    @PostMapping(ApiPaths.CREATE_SEMD_DRUG)
    public String createDrugPreferentialRecipe(@RequestBody @Valid @NotNull FullRecipe fullRecipe) {
        return semdService.createDrugPreferentialRecipe(fullRecipe);
    }

    @Operation(summary = "Льготный рецепт на мед. изделие")
    @PostMapping(ApiPaths.CREATE_SEMD_MEDICAL_PRODUCT)
    public String createMedicalProductPreferentialRecipe(@RequestBody @Valid @NotNull FullRecipe fullRecipe) {
        return semdService.createMedicalProductPreferentialRecipe(fullRecipe);
    }

    @Operation(summary = "Льготный рецепт на специализированный продукт лечебного питания")
    @PostMapping(ApiPaths.CREATE_SEMD_HEALTH_FOOD)
    public String createHealthFoodPreferentialRecipe(@RequestBody @Valid @NotNull FullRecipe fullRecipe) {
        return semdService.createHealthFoodPreferentialRecipe(fullRecipe);
    }
}
