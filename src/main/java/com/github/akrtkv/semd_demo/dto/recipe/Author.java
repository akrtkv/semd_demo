package su.medsoft.rir.recipe.dto.rir.recipe;

public class Author extends Staff {

    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Author{" +
                "phone='" + phone + '\'' +
                '}';
    }
}
