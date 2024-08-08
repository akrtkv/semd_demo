package su.medsoft.rir.recipe.dto.egisz.callback;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class UploadStatus {

    @XmlElement
    private UploadStatusElement element;

    public UploadStatusElement getElement() {
        return element;
    }

    public void setElement(UploadStatusElement element) {
        this.element = element;
    }
}
