package su.medsoft.rir.recipe.dto.rir.recipe;

import javax.validation.constraints.NotEmpty;

public class ProviderOrganization extends Organisation {

    @NotEmpty
    private String ogrn;

    @NotEmpty
    private String phone;

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "ProviderOrganization{" +
                "ogrn='" + ogrn + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
