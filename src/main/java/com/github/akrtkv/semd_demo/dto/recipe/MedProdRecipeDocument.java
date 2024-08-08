package com.github.akrtkv.semd_demo.dto.recipe;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class MedProdRecipeDocument {

    @NotEmpty
    private String misId;

    @NotNull
    @Valid
    private MedicalProductRecipe medicalProductRecipe;

    public String getMisId() {
        return misId;
    }

    public void setMisId(String misId) {
        this.misId = misId;
    }

    public MedicalProductRecipe getMedicalProductRecipe() {
        return medicalProductRecipe;
    }

    public void setMedicalProductRecipe(MedicalProductRecipe medicalProductRecipe) {
        this.medicalProductRecipe = medicalProductRecipe;
    }
}
