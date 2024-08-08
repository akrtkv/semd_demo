package su.medsoft.rir.recipe.dto.rir.recipe;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import su.medsoft.rir.recipe.utils.LocalDateDeserializer;
import su.medsoft.rir.recipe.utils.LocalDateSerializer;

import java.time.LocalDate;

public class FrlloData {

    @Schema(description = "ФРЛЛО ID Рецепта")
    private String recipeId;

    @Schema(description = "Дата регистрации в ФРЛЛО")
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate registrationDate;

    @Schema(description = "Номер регистровой записи ФРЛЛО")
    private String registrationNumber;

    public String getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}
