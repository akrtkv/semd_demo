package com.github.akrtkv.semd_demo.dto.recipe;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalProduct {

    private Long id;

    @Schema(description = "Дата отпуска медицинской продукции")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Schema(description = "Код медицинского изделия по справочнику 1.2.643.5.1.13.13.99.2.604")
    @NotEmpty
    private String code;

    @Schema(description = "Наименование медицинского изделия по справочнику 1.2.643.5.1.13.13.99.2.604")
    @NotEmpty
    private String name;

    @Schema(description = "Количество медицинского изделия")
    @NotNull
    private Integer amount;

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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
