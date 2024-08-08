package com.github.akrtkv.semd_demo.dto.recipe;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public class MedicalProductRecipe extends Recipe {

    @Schema(description = "Медицинское изделие")
    @NotNull
    @Valid
    private MedicalProduct medicalProduct;

    public MedicalProduct getMedicalProduct() {
        return medicalProduct;
    }

    public void setMedicalProduct(MedicalProduct medicalProduct) {
        this.medicalProduct = medicalProduct;
    }
}
