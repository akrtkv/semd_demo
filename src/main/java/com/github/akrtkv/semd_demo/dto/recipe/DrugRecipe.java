package su.medsoft.rir.recipe.dto.rir.recipe;

import io.swagger.v3.oas.annotations.media.Schema;
import su.medsoft.rir.recipe.validator.NotNullIfAnotherFieldFilled;
import su.medsoft.rir.recipe.validator.NotNullIfAnotherFieldNull;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

@NotNullIfAnotherFieldNull(fieldName = "drug", dependFieldName = "drugs")
@NotNullIfAnotherFieldNull(fieldName = "drugs", dependFieldName = "drug")
@NotNullIfAnotherFieldFilled(fieldName = "mkb", dependFieldName = "drug")
@NotNullIfAnotherFieldFilled(fieldName = "financing", dependFieldName = "drug")
@NotNullIfAnotherFieldFilled(fieldName = "benefit", dependFieldName = "drug")
public class DrugRecipe extends Recipe {

    @Schema(description = "Лекарственное изделие (льготный рецепт)")
    @Valid
    private Drug drug;

    @Schema(description = "Лекарственные изделия (обычный рецепт)")
    @Size(min = 1, max = 3)
    private List<@Valid Drug> drugs;

    public DrugRecipe() {
    }

    public DrugRecipe(Recipe recipe) {
        super(recipe);
    }

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

    @Override
    public String toString() {
        return "DrugRecipe{" +
                "drug=" + drug +
                ", drugs=" + drugs +
                '}';
    }
}
