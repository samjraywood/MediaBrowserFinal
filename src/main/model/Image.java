package main.model;

public class Image {

    private FileName fileName;
    private FilePath filePath;

    public Image() {
    }

    public FileName getFileName() {
        return fileName;
    }

    public void setFileName(final FileName fileName) {
        this.fileName = fileName;
    }

    public FilePath getFilePath() {
        return filePath;
    }

    public void setFilePath(final FilePath filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "Image{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
