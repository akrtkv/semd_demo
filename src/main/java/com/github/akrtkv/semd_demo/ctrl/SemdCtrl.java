package com.github.akrtkv.semd_demo.ctrl;

import com.github.akrtkv.semd_demo.service.IemkService;
import com.github.akrtkv.semd_demo.util.ApiPaths;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Validated
public class IemkCtrl {

    private final IemkService iemkService;

    @Autowired
    public IemkCtrl(IemkService iemkService) {
        this.iemkService = iemkService;
    }

    @PostMapping(value = ApiPaths.CREATE_SEMD_DRUG)
    public String createDrugPreferentialRecipe(@@RequestBody @Valid @NotNull DrugRecipe drugRecipe) {
        return iemkService.createDrugPreferentialRecipe(drugRecipe);
    }

    @PostMapping(value = ApiPaths.CREATE_SEMD_MEDICAL_PRODUCT)
    public String createMedicalProductPreferentialRecipe(@RequestPart("medicalProductRecipe") @NotNull @Valid MedicalProductRecipe medicalProductRecipe,) {
        return iemkService.createMedicalProductPreferentialRecipe(medicalProductRecipe);
    }

    @PostMapping(value = ApiPaths.CREATE_SEMD_HEALTH_FOOD)
    public String createHealthFoodPreferentialRecipe(@RequestPart("healthFoodRecipe") @NotNull @Valid HealthFoodRecipe healthFoodRecipe) {
        return iemkService.createHealthFoodPreferentialRecipe(healthFoodRecipe);
    }
}
