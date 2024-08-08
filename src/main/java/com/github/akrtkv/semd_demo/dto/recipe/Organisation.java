package su.medsoft.rir.recipe.dto.rir.recipe;

import io.swagger.v3.oas.annotations.media.Schema;
import su.medsoft.rir.recipe.dto.rir.Address;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Organisation {

    private Long id;

    @Schema(description = "По справочнику «Реестр медицинских организаций Российской Федерации» (OID: 1.2.643.5.1.13.13.11.1461)")
    @NotEmpty
    private String oid;

    @NotEmpty
    private String name;

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

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Organisation{" +
                "id=" + id +
                ", oid='" + oid + '\'' +
                ", name='" + name + '\'' +
                ", address=" + address +
                '}';
    }
}
