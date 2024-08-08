package com.github.akrtkv.semd_demo.dto.recipe;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.akrtkv.semd_demo.dto.Patient;
import com.github.akrtkv.semd_demo.util.ZonedDateTimeDeserializer;
import com.github.akrtkv.semd_demo.util.ZonedDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

public class Recipe {

    private Long id;

    @NotEmpty
    private String misId;

    @Schema(description = "Название по классификатору 1.2.643.5.1.13.2.1.1.646")
    @NotEmpty
    private String name;

    @Schema(description = "Дата создания документа (Должен быть с точностью до дня, но следует быть с точностью до минут)")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @NotNull
    private ZonedDateTime createDate;

    @Schema(description = "Идентификатор набора версий документа")
    @NotNull
    private Integer versionId;

    @Schema(description = "Номер версии данного документа")
    @NotNull
    private Integer versionNumber;

    @NotNull
    @Valid
    private Patient patient;

    @Schema(description = "Медицинская организация, оформившая рецепт")
    @NotNull
    @Valid
    private ProviderOrganization providerOrganization;

    @Schema(description = "Дата подписи документа автором")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @NotNull
    private ZonedDateTime signatureDate;

    @Schema(description = "Автор рецепта")
    @NotNull
    @Valid
    private Author author;

    @Schema(description = "Данные об организации владельце документа")
    @NotNull
    @Valid
    private Organisation custodianOrganisation;

    @Schema(description = "Дата подписи документа лицом, придавшем юридическую силу документу")
    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    @NotNull
    private ZonedDateTime legalAuthenticatorSignatureDate;

    @Schema(description = "Данные о лице придавшем юридическую силу документу")
    @NotNull
    @Valid
    private Staff legalAuthenticator;

    @Schema(description = "Сведения о случае оказания медицинской помощи")
    @NotNull
    @Valid
    private MedicalCareCase medicalCareCase;

    @Schema(description = "Серия рецепта")
    @NotEmpty
    private String series;

    @Schema(description = "Номер рецепта")
    @NotEmpty
    private String number;

    @Schema(description = "Приоритет исполнения по справочнику 1.2.643.5.1.13.13.99.2.609 (на русском языке)")
    private String priority;

    @Schema(description = "Протокол врачебной комиссии")
    @Valid
    private MedicalCommissionProtocol medicalCommissionProtocol;

    @Schema(description = "Срок действия рецепта (Пример: 3 месяца)\n" +
            "Доступные сроки действия рецепта на лекарственный препарат или специализированный продукт лечебного питания: 15 дней, 30 дней и 90 дней\n" +
            "Доступные сроки действия рецепта на медицинское изделие: 1 месяц, 3 месяца")
    @NotEmpty
    private String validity;

    @Schema(description = "Дата окончания действия рецепта")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @Schema(description = "По специальному назначению")
    private boolean special;

    @Schema(description = "Пациенту с хроническим заболеванием")
    private boolean chronicDisease;

    @Schema(description = "Заболевание по МКБ-10")
    @Valid
    private Mkb mkb;

    @Schema(description = "Финансирование")
    @Valid
    private Financing financing;

    @Schema(description = "Льгота")
    @Valid
    private Benefit benefit;

    @Schema(description = "Регистрационные данные ФРЛЛО")
    private FrlloData frlloData;

    public Recipe() {
    }

    public Recipe(Recipe recipe) {
        this.id = recipe.id;
        this.misId = recipe.misId;
        this.name = recipe.name;
        this.createDate = recipe.createDate;
        this.versionId = recipe.versionId;
        this.versionNumber = recipe.versionNumber;
        this.patient = recipe.patient;
        this.providerOrganization = recipe.providerOrganization;
        this.signatureDate = recipe.signatureDate;
        this.author = recipe.author;
        this.custodianOrganisation = recipe.custodianOrganisation;
        this.legalAuthenticatorSignatureDate = recipe.legalAuthenticatorSignatureDate;
        this.legalAuthenticator = recipe.legalAuthenticator;
        this.medicalCareCase = recipe.medicalCareCase;
        this.series = recipe.series;
        this.number = recipe.number;
        this.priority = recipe.priority;
        this.medicalCommissionProtocol = recipe.medicalCommissionProtocol;
        this.validity = recipe.validity;
        this.endDate = recipe.endDate;
        this.special = recipe.special;
        this.chronicDisease = recipe.chronicDisease;
        this.mkb = recipe.mkb;
        this.financing = recipe.financing;
        this.benefit = recipe.benefit;
        this.frlloData = recipe.frlloData;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMisId() {
        return misId;
    }

    public void setMisId(String misId) {
        this.misId = misId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(ZonedDateTime createDate) {
        this.createDate = createDate;
    }

    public Integer getVersionId() {
        return versionId;
    }

    public void setVersionId(Integer versionId) {
        this.versionId = versionId;
    }

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public ProviderOrganization getProviderOrganization() {
        return providerOrganization;
    }

    public void setProviderOrganization(ProviderOrganization providerOrganization) {
        this.providerOrganization = providerOrganization;
    }

    public ZonedDateTime getSignatureDate() {
        return signatureDate;
    }

    public void setSignatureDate(ZonedDateTime signatureDate) {
        this.signatureDate = signatureDate;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Organisation getCustodianOrganisation() {
        return custodianOrganisation;
    }

    public void setCustodianOrganisation(Organisation custodianOrganisation) {
        this.custodianOrganisation = custodianOrganisation;
    }

    public ZonedDateTime getLegalAuthenticatorSignatureDate() {
        return legalAuthenticatorSignatureDate;
    }

    public void setLegalAuthenticatorSignatureDate(ZonedDateTime legalAuthenticatorSignatureDate) {
        this.legalAuthenticatorSignatureDate = legalAuthenticatorSignatureDate;
    }

    public Staff getLegalAuthenticator() {
        return legalAuthenticator;
    }

    public void setLegalAuthenticator(Staff legalAuthenticator) {
        this.legalAuthenticator = legalAuthenticator;
    }

    public MedicalCareCase getMedicalCareCase() {
        return medicalCareCase;
    }

    public void setMedicalCareCase(MedicalCareCase medicalCareCase) {
        this.medicalCareCase = medicalCareCase;
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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public MedicalCommissionProtocol getMedicalCommissionProtocol() {
        return medicalCommissionProtocol;
    }

    public void setMedicalCommissionProtocol(MedicalCommissionProtocol medicalCommissionProtocol) {
        this.medicalCommissionProtocol = medicalCommissionProtocol;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean isSpecial() {
        return special;
    }

    public void setSpecial(boolean special) {
        this.special = special;
    }

    public boolean isChronicDisease() {
        return chronicDisease;
    }

    public void setChronicDisease(boolean chronicDisease) {
        this.chronicDisease = chronicDisease;
    }

    public Mkb getMkb() {
        return mkb;
    }

    public void setMkb(Mkb mkb) {
        this.mkb = mkb;
    }

    public Financing getFinancing() {
        return financing;
    }

    public void setFinancing(Financing financing) {
        this.financing = financing;
    }

    public Benefit getBenefit() {
        return benefit;
    }

    public void setBenefit(Benefit benefit) {
        this.benefit = benefit;
    }

    public FrlloData getFrlloData() {
        return frlloData;
    }

    public void setFrlloData(FrlloData frlloData) {
        this.frlloData = frlloData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return misId.equals(recipe.misId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(misId);
    }
}
