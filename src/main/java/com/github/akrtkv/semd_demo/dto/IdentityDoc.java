package com.github.akrtkv.semd_demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class IdentityDoc {

    private Long id;

    @Schema(description = "Код документа по справочнику 1.2.643.5.1.13.13.99.2.631")
    @NotEmpty
    private String code;

    @Schema(description = "Название документа по справочнику 1.2.643.5.1.13.13.99.2.631")
    @NotEmpty
    private String name;

    @NotEmpty
    private String series;

    @NotEmpty
    private String number;

    @Schema(description = "Кем выдан документ")
    @NotEmpty
    private String issueOrgName;

    @Schema(description = "Кем выдан документ, код подразделения")
    @NotEmpty
    private String issueOrgCode;

    @Schema(description = "Дата выдачи документа")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;

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

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getIssueOrgName() {
        return issueOrgName;
    }

    public void setIssueOrgName(String issueOrgName) {
        this.issueOrgName = issueOrgName;
    }

    public String getIssueOrgCode() {
        return issueOrgCode;
    }

    public void setIssueOrgCode(String issueOrgCode) {
        this.issueOrgCode = issueOrgCode;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
