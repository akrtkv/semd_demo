package su.medsoft.rir.recipe.dto.rir.recipe;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import su.medsoft.rir.recipe.utils.ZonedDateTimeDeserializer;
import su.medsoft.rir.recipe.utils.ZonedDateTimeSerializer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public class MedicalCareCase {

    private Long id;

    @Schema(description = "Уникальный идентификатор случая оказания медицинской помощи")
    @NotNull
    private Long misId;

    @Schema(description = "Номер медицинской карты")
    @NotEmpty
    private String medicalCardNumber;

    @Schema(description = "Дата начала случая")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @NotNull
    private ZonedDateTime beginDate;

    @Schema(description = "Дата окончания случая")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @NotNull
    private ZonedDateTime endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMisId() {
        return misId;
    }

    public void setMisId(Long misId) {
        this.misId = misId;
    }

    public String getMedicalCardNumber() {
        return medicalCardNumber;
    }

    public void setMedicalCardNumber(String medicalCardNumber) {
        this.medicalCardNumber = medicalCardNumber;
    }

    public ZonedDateTime getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(ZonedDateTime beginDate) {
        this.beginDate = beginDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "MedicalCareCase{" +
                "id=" + id +
                ", misId=" + misId +
                ", medicalCardNumber='" + medicalCardNumber + '\'' +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate +
                '}';
    }
}
