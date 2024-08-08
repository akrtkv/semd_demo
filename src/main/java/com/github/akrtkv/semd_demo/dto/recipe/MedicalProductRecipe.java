package su.medsoft.rir.recipe.dto.rir.recipe;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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

    @Override
    public String toString() {
        return "MedicalProductRecipe{" +
                "medicalProduct=" + medicalProduct +
                '}';
    }
}
