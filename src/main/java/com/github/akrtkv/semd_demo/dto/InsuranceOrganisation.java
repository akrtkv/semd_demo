package com.github.akrtkv.semd_demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class InsuranceOrganisation {

    private Long id;

    @Schema(description = "Идентификатор страховой компании по справочнику 1.2.643.5.1.13.13.99.2.183")
    @NotNull
    private Integer nsiId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String phone;

    @Schema(description = "Адрес организации")
    @NotNull
    @Valid
    private Address address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNsiId() {
        return nsiId;
    }

    public void setNsiId(Integer nsiId) {
        this.nsiId = nsiId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
