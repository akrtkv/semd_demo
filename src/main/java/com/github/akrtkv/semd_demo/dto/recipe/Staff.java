package su.medsoft.rir.recipe.dto.rir.recipe;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class Staff {

    private Long id;

    @NotEmpty
    private String misId;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String firstName;

    private String middleName;

    @NotEmpty
    @Pattern(regexp = "^(?:[- ]*\\d){11}$")
    private String snils;

    @Schema(description = "Должность по справочнику 1.2.643.5.1.13.13.11.1002")
    @NotNull
    @Valid
    private Post post;

    @Schema(description = "Место работы")
    @NotNull
    @Valid
    private Organisation representedOrganisation;

    @Schema(description = "Код врача")
    private String code;

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

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Organisation getRepresentedOrganisation() {
        return representedOrganisation;
    }

    public void setRepresentedOrganisation(Organisation representedOrganisation) {
        this.representedOrganisation = representedOrganisation;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", misId='" + misId + '\'' +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", snils='" + snils + '\'' +
                ", post=" + post +
                ", representedOrganisation=" + representedOrganisation +
                ", code='" + code + '\'' +
                '}';
    }
}
