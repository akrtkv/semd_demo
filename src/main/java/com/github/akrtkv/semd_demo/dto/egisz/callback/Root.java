package su.medsoft.rir.recipe.dto.egisz.callback;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Root {

    @XmlElement(name = "rec_qty")
    private Integer recorded;

    @XmlElement(name = "upload_status")
    private UploadStatus uploadStatus;

    @XmlElement
    private Result result;

    public Integer getRecorded() {
        return recorded;
    }

    public void setRecorded(Integer recorded) {
        this.recorded = recorded;
    }

    public UploadStatus getUploadStatus() {
        return uploadStatus;
    }

    public void setUploadStatus(UploadStatus uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
