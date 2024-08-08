package com.github.akrtkv.semd_demo.dto.recipe;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class MedProdRecipeRequest {

    @NotNull
    private List<@Valid MedProdRecipeDocument> medProdRecipeDocuments;

    public List<MedProdRecipeDocument> getMedProdRecipeDocuments() {
        return medProdRecipeDocuments;
    }

    public void setMedProdRecipeDocuments(List<MedProdRecipeDocument> medProdRecipeDocuments) {
        this.medProdRecipeDocuments = medProdRecipeDocuments;
    }
}
