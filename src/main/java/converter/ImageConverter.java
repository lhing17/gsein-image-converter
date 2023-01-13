package converter;

import image.Image;
import image.ImageFormat;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ImageConverter {

    protected List<Image> sourceImages = new ArrayList<>();

    protected Path targetDir;

    protected ImageFormat targetFormat;

    protected boolean writeToAlpha = true;


    public synchronized ImageConverter addImage(Image image) {
        sourceImages.add(image);
        return this;
    }

    public synchronized ImageConverter addImages(Image... images) {
        sourceImages.addAll(Arrays.asList(images));
        return this;
    }

    public synchronized ImageConverter targetDir(Path targetDir) {
        this.targetDir = targetDir;
        return this;
    }

    public synchronized ImageConverter targetFormat(ImageFormat targetFormat) {
        this.targetFormat = targetFormat;
        return this;
    }

    public synchronized ImageConverter writeToAlpha(boolean writeToAlpha) {
        this.writeToAlpha = writeToAlpha;
        return this;
    }

    public abstract void convert();

    public static ImageConverter getInstance(ConvertMode mode) {
        return switch (mode) {
            case FORMAT -> new FormatConverter();
            case SIZE, ZIP, PDF, WATERMARK -> throw new UnsupportedOperationException("暂不支持该功能");
        };
    }
}
