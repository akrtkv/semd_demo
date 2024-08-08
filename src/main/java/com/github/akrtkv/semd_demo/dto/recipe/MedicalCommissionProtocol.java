package su.medsoft.rir.recipe.dto.rir.recipe;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import su.medsoft.rir.recipe.utils.ZonedDateTimeDeserializer;
import su.medsoft.rir.recipe.utils.ZonedDateTimeSerializer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public class MedicalCommissionProtocol {

    private Long id;

    @NotEmpty
    private String number;

    @Schema(description = "Дата и время проведения врачебной комиссии")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @NotNull
    private ZonedDateTime date;

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

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "MedicalCommissionProtocol{" +
                "id=" + id +
                ", number='" + number + '\'' +
                ", date=" + date +
                '}';
    }
}
