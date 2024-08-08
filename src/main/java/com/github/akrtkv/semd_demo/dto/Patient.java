package com.github.akrtkv.semd_demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.akrtkv.semd_demo.validator.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public class Patient {

    private Long id;

    @NotEmpty
    private String misId;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String firstName;

    private String middleName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @Schema(description = "М или Ж")
    @NotEmpty
    @Gender
    private String gender;

    @NotEmpty
    @Pattern(regexp = "^(?:[- ]*\\d){11}$")
    private String snils;

    @Schema(description = "Сведения о страховом полисе ОМС")
    @Valid
    private Insurance insurance;

    @Schema(description = "Документ, удостоверяющий личность получателя, серия, номер, кем выдан. По справочнику 1.2.643.5.1.13.13.99.2.48")
    @Valid
    private IdentityDoc identityDoc;

    @Schema(description = "Адрес постоянной регистрации пациента")
    @Valid
    @NotNull
    private Address address;

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    public Insurance getInsurance() {
        return insurance;
    }

    public void setInsurance(Insurance insurance) {
        this.insurance = insurance;
    }

    public IdentityDoc getIdentityDoc() {
        return identityDoc;
    }

    public void setIdentityDoc(IdentityDoc identityDoc) {
        this.identityDoc = identityDoc;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
