package com.github.akrtkv.semd_demo.dto.recipe;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class HealthFood {

    private Long id;

    @Schema(description = "Дата отпуска медицинской продукции")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Schema(description = "Длительность приема препарата")
    @NotNull
    private Integer durationDays;

    @Schema(description = "Код лечебного питания по справочнику 1.2.643.5.1.13.13.99.2.603")
    @NotEmpty
    private String code;

    @Schema(description = "Название лечебного питания по справочнику 1.2.643.5.1.13.13.99.2.603")
    @NotEmpty
    private String name;

    @Schema(description = "Описание лечебного питания по справочнику 1.2.643.5.1.13.13.99.2.603")
    @NotEmpty
    private String description;

    @Schema(description = "Код пути введения препарата по справочнику 1.2.643.5.1.13.13.11.1468")
    @NotNull
    private Integer routeCode;

    @Schema(description = "Название пути введения препарата по справочнику 1.2.643.5.1.13.13.11.1468")
    @NotEmpty
    private String routeName;

    @Schema(description = "Количество назначенных доз")
    @NotNull
    private Integer dose;

    @Schema(description = "Цена в рублях за единицу отпущенной потребительской упаковки")
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getDurationDays() {
        return durationDays;
    }

    public void setDurationDays(Integer durationDays) {
        this.durationDays = durationDays;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getDose() {
        return dose;
    }

    public void setDose(Integer dose) {
        this.dose = dose;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
