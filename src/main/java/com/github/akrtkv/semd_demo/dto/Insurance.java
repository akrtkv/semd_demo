package su.medsoft.rir.recipe.dto.rir;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Insurance {

    private Long id;

    @Schema(description = "Номера полиса пациента нового образца")
    @NotEmpty
    private String number;

    @Schema(description = "Страховая компания")
    @NotNull
    @Valid
    private InsuranceOrganisation insuranceOrganisation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public InsuranceOrganisation getInsuranceOrganisation() {
        return insuranceOrganisation;
    }

    public void setInsuranceOrganisation(InsuranceOrganisation insuranceOrganisation) {
        this.insuranceOrganisation = insuranceOrganisation;
    }

    @Override
    public String toString() {
        return "Insurance{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", insuranceOrganisation=" + insuranceOrganisation +
                '}';
    }
}
