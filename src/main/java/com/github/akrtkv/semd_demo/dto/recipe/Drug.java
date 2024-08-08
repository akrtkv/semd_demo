package su.medsoft.rir.recipe.dto.rir.recipe;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Drug {

    private Long id;

    @Schema(description = "Длительность приема препарата")
    @NotNull
    private Integer durationDays;

    @Schema(description = "Дата отпуска медицинской продукции")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Schema(description = "Код СМНН по справочнику 1.2.643.5.1.13.13.99.2.540")
    @NotEmpty
    private String codeMnn;

    @Schema(description = "Код КЛП по справочнику 1.2.643.5.1.13.13.99.2.540")
    private String codeKlp;

    @Schema(description = "Торговое наименование по справочнику 1.2.643.5.1.13.13.99.2.540")
    private String tradeName;

    @Schema(description = "Стандартизованное МНН по справочнику 1.2.643.5.1.13.13.99.2.540")
    @NotEmpty
    private String name;

    @Schema(description = "Код пути введения препарата по справочнику 1.2.643.5.1.13.13.11.1468")
    @NotNull
    private Integer routeCode;

    @Schema(description = "Название пути введения препарата по справочнику 1.2.643.5.1.13.13.11.1468")
    @NotEmpty
    private String routeName;

    @Schema(description = "Количество отпущенных потребительских упаковок")
    private Integer packAmount;

    @Schema(description = "Цена в рублях за единицу отпущенной потребительской упаковки")
    private BigDecimal price;

    @Schema(description = "Количество назначенных доз")
    @NotNull
    private Integer dose;

    @Schema(description = "Разовая доза. (Для обычного рецепта)")
    private Short singleDose;

    @Schema(description = "Код разовой дозы. Справочник 1.2.643.5.1.13.13.99.2.612 версия 1.28. (Для обычного рецепта)")
    private Short singleDoseCode;

    @Schema(description = "Название разовой дозы. Название. Справочник 1.2.643.5.1.13.13.99.2.612 версия 1.28. (Для обычного рецепта)")
    private String singleDoseName;

    @Schema(description = "Единица разовой дозы. Примеры: таблетка, капсула. (Для обычного рецепта)")
    private String singleDoseUnit;

    @Schema(description = "Кратность приема препарата. (Для обычного рецепта)")
    private Short period;

    @Schema(description = "Кратность приема препарата. Код UCUM. Справочник 1.2.643.5.1.13.13.11.1358 версия 2.5. (Для обычного рецепта)")
    private String periodUnit;

    @Schema(description = "Кратность приема препарата. Идентификатор. Справочник 1.2.643.5.1.13.13.11.1358 версия 2.5. (Для обычного рецепта)")
    private Short periodCode;

    @Schema(description = "Кратность приема препарата. Краткое наименование. Справочник 1.2.643.5.1.13.13.11.1358 версия 2.5. (Для обычного рецепта)")
    private String periodName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getCodeMnn() {
        return codeMnn;
    }

    public void setCodeMnn(String codeMnn) {
        this.codeMnn = codeMnn;
    }

    public String getCodeKlp() {
        return codeKlp;
    }

    public void setCodeKlp(String codeKlp) {
        this.codeKlp = codeKlp;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRouteCode() {
        return routeCode;
    }

    public void setRouteCode(Integer routeCode) {
        this.routeCode = routeCode;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Integer getPackAmount() {
        return packAmount;
    }

    public void setPackAmount(Integer packAmount) {
        this.packAmount = packAmount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDose() {
        return dose;
    }

    public void setDose(Integer dose) {
        this.dose = dose;
    }

    public Short getSingleDose() {
        return singleDose;
    }

    public void setSingleDose(Short singleDose) {
        this.singleDose = singleDose;
    }

    public Short getSingleDoseCode() {
        return singleDoseCode;
    }

    public void setSingleDoseCode(Short singleDoseCode) {
        this.singleDoseCode = singleDoseCode;
    }

    public String getSingleDoseName() {
        return singleDoseName;
    }

    public void setSingleDoseName(String singleDoseName) {
        this.singleDoseName = singleDoseName;
    }

    public String getSingleDoseUnit() {
        return singleDoseUnit;
    }

    public void setSingleDoseUnit(String singleDoseUnit) {
        this.singleDoseUnit = singleDoseUnit;
    }

    public Short getPeriod() {
        return period;
    }

    public void setPeriod(Short period) {
        this.period = period;
    }

    public String getPeriodUnit() {
        return periodUnit;
    }

    public void setPeriodUnit(String periodUnit) {
        this.periodUnit = periodUnit;
    }

    public Short getPeriodCode() {
        return periodCode;
    }

    public void setPeriodCode(Short periodCode) {
        this.periodCode = periodCode;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    @Override
    public String toString() {
        return "Drug{" +
                "id=" + id +
                ", durationDays=" + durationDays +
                ", releaseDate=" + releaseDate +
                ", codeMnn='" + codeMnn + '\'' +
                ", codeKlp='" + codeKlp + '\'' +
                ", tradeName='" + tradeName + '\'' +
                ", name='" + name + '\'' +
                ", routeCode=" + routeCode +
                ", routeName='" + routeName + '\'' +
                ", packAmount=" + packAmount +
                ", price=" + price +
                ", dose=" + dose +
                ", singleDose=" + singleDose +
                ", singleDoseCode=" + singleDoseCode +
                ", singleDoseName='" + singleDoseName + '\'' +
                ", period=" + period +
                ", periodUnit='" + periodUnit + '\'' +
                ", periodCode='" + periodCode + '\'' +
                ", periodName='" + periodName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drug drug = (Drug) o;
        return Objects.equals(id, drug.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
