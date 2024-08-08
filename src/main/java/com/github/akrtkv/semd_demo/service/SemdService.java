package com.github.akrtkv.semd_demo.service;

import com.github.akrtkv.semd_demo.dto.recipe.FullRecipe;
import com.github.akrtkv.semd_demo.util.ObjectCreator;
import org.springframework.stereotype.Service;

@Service
public class SemdService {

    private final ObjectCreator objectCreator;

    public SemdService(ObjectCreator objectCreator) {
        this.objectCreator = objectCreator;
    }

    public String createDrugPreferentialRecipe(FullRecipe fullRecipe) {
        return objectCreator.createSemdDrugXml(fullRecipe, 4);
    }

    public String createMedicalProductPreferentialRecipe(FullRecipe fullRecipe) {
        return objectCreator.createSemdMedicalProductXml(fullRecipe, 4);
    }

    public String createHealthFoodPreferentialRecipe(FullRecipe fullRecipe) {
        return objectCreator.createSemdHealthFoodXml(fullRecipe, 4);
    }
}
