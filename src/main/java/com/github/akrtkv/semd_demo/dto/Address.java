package com.github.akrtkv.semd_demo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public class Address {

    private Long id;

    @Schema(description = "Адрес текстом")
    @NotEmpty
    private String fullAddress;

    @Schema(description = "Субъект РФ (Код ФНС по справочнику \"Субъекты Российской Федерации\" (OID:1.2.643.5.1.13.13.99.2.206))")
    @NotEmpty
    private String regionCode;

    @Schema(description = "Субъект РФ (Код ФНС по справочнику \"Субъекты Российской Федерации\" (OID:1.2.643.5.1.13.13.99.2.206))")
    @NotEmpty
    private String regionName;

    @Schema(description = "Глобальный уникальный идентификатор адресного объекта")
    private String aoGuid;

    @Schema(description = "Глобальный уникальный идентификатор дома")
    private String houseGuid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getAoGuid() {
        return aoGuid;
    }

    public void setAoGuid(String aoGuid) {
        this.aoGuid = aoGuid;
    }

    public String getHouseGuid() {
        return houseGuid;
    }

    public void setHouseGuid(String houseGuid) {
        this.houseGuid = houseGuid;
    }
}
