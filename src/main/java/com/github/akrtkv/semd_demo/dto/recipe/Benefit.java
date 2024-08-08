package su.medsoft.rir.recipe.dto.rir.recipe;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Benefit {

    private Long id;

    @Schema(description = "Код типа назначения льготного рецепта по справочнику 1.2.643.5.1.13.13.99.2.651")
    @NotNull
    private Integer typeCode;

    @Schema(description = "Название типа назначения льготного рецепта по справочнику 1.2.643.5.1.13.13.99.2.651")
    @NotEmpty
    private String typeName;

    @Schema(description = "Код льготной категории по справочнику 1.2.643.5.1.13.13.99.2.541")
    private String code;

    @Schema(description = "Название льготной категории по справочнику 1.2.643.5.1.13.13.99.2.541")
    private String name;

    @Schema(description = "Региональный код льготной категории")
    private String regionCode;

    @Schema(description = "Региональное название льготной категории")
    private String regionName;

    @Schema(description = "Размер льготы по справочнику 1.2.643.5.1.13.13.99.2.605")
    @NotEmpty
    private String amount;

    @Schema(description = "Размер льготы в процентах")
    @NotEmpty
    private String percent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "Benefit{" +
                "id=" + id +
                ", typeCode=" + typeCode +
                ", typeName='" + typeName + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", regionCode='" + regionCode + '\'' +
                ", regionName='" + regionName + '\'' +
                ", amount='" + amount + '\'' +
                ", percent='" + percent + '\'' +
                '}';
    }
}
