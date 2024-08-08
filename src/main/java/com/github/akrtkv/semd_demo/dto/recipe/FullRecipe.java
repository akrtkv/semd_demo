package com.github.akrtkv.semd_demo.dto.recipe;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class FullRecipe extends Recipe {

    @Schema(description = "Для льготного рецепта")
    private Drug drug;

    @Schema(description = "Для обычного рецепта")
    private List<Drug> drugs;

    private HealthFood healthFood;

    private MedicalProduct medicalProduct;

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public List<Drug> getDrugs() {
        return drugs;
    }

    public void setDrugs(List<Drug> drugs) {
        this.drugs = drugs;
    }

    public HealthFood getHealthFood() {
        return healthFood;
    }

    public void setHealthFood(HealthFood healthFood) {
        this.healthFood = healthFood;
    }

    public MedicalProduct getMedicalProduct() {
        return medicalProduct;
    }

    public void setMedicalProduct(MedicalProduct medicalProduct) {
        this.medicalProduct = medicalProduct;
    }
}
