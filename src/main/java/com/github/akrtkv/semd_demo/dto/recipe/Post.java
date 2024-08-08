package su.medsoft.rir.recipe.dto.rir.recipe;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Post {

    @NotNull
    private Integer code;

    @NotEmpty
    private String name;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Post{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
