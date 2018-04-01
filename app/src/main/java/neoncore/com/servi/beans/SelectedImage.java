package neoncore.com.servi.beans;

/**
 * Created by Musa on 3/5/2018.
 */

public class SelectedImage {

    String fileName;
    String size;
    String uri;

    public SelectedImage(String fileName, String size, String uri) {
        this.fileName = fileName;
        this.size = size;
        this.uri = uri;
    }

    public SelectedImage() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
