package image;

import exception.ErrorCode;
import exception.GseinConvertException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

public class Image {
    private String name;
    private ImageFormat format;
    private BufferedImage bufferedImage;

    private static final AtomicInteger generateId = new AtomicInteger(0);



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageFormat getFormat() {
        return format;
    }

    public void setFormat(ImageFormat format) {
        this.format = format;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    private Image() {
    }

    public static Image fromFile(File file) {
        Image image = new Image();
        image.setName(file.getName());
        image.setFormat(getFormatFromFileName(file.getName()));
        try {
            image.setBufferedImage(ImageIO.read(file));
        } catch (IOException e) {
            throw new GseinConvertException(ErrorCode.SOURCE_FILE_NOT_FOUND);
        }
        return image;
    }

    public static Image fromPath(Path path) {
        return fromFile(path.toFile());
    }

    public static Image fromPath(String path) {
        return fromFile(new File(path));
    }

    public static Image fromInputStream(InputStream inputStream, String fileName) {
        Image image = new Image();
        image.setName(fileName);
        image.setFormat(getFormatFromFileName(fileName));
        try {
            image.setBufferedImage(ImageIO.read(inputStream));
        } catch (IOException e) {
            throw new GseinConvertException(ErrorCode.SOURCE_FILE_NOT_FOUND);
        }
        return image;
    }

    public static Image fromBase64(String base64) {
        Image image = new Image();
        image.setFormat(getFormatFromBase64(base64));
        image.setName(generateId.getAndIncrement() + "." + image.getFormat().name().toLowerCase());
        return image;
    }

    private static ImageFormat getFormatFromBase64(String base64) {
        if (base64.startsWith("data:image/png;base64,")) {
            return ImageFormat.PNG;
        } else if (base64.startsWith("data:image/jpeg;base64,")) {
            return ImageFormat.JPEG;
        } else if (base64.startsWith("data:image/gif;base64,")) {
            return ImageFormat.GIF;
        } else if (base64.startsWith("data:image/bmp;base64,")) {
            return ImageFormat.BMP;
        } else if (base64.startsWith("data:image/tiff;base64,")) {
            return ImageFormat.TIFF;
        } else if (base64.startsWith("data:image/webp;base64,")) {
            return ImageFormat.WEBP;
        } else if (base64.startsWith("data:image/tga;base64,")) {
            return ImageFormat.TGA;
        } else {
            throw new GseinConvertException(ErrorCode.UNKNOWN_FORMAT);
        }
    }

    private static ImageFormat getFormatFromFileName(String fileName) {
        String[] split = fileName.split("\\.");
        String format = split[split.length - 1];
        return ImageFormat.valueOf(format.toUpperCase());
    }

}
