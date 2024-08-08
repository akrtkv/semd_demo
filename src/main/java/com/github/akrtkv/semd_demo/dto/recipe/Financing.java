package com.github.akrtkv.semd_demo.dto.recipe;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public class Financing {

    private Long id;

    @Schema(description = "Код источника финансирования по справочнику 1.2.643.5.1.13.13.99.2.541")
    @NotEmpty
    private String code;

    @Schema(description = "Название источника финансирования по справочнику 1.2.643.5.1.13.13.99.2.541")
    @NotEmpty
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
