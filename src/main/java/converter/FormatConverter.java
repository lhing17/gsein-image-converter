package converter;

import exception.ErrorCode;
import exception.GseinConvertException;
import image.CustomImageFilter;
import image.Image;
import image.ImageFormat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FormatConverter extends ImageConverter {
    @Override
    public void convert() {
        // guard语句
        if (sourceImages.isEmpty()) {
            throw new GseinConvertException(ErrorCode.SOURCE_IMAGE_LIST_EMPTY);
        }
        if (targetDir == null) {
            throw new GseinConvertException(ErrorCode.TARGET_PATH_EMPTY);
        }
        if (!Files.exists(targetDir)) {
            throw new GseinConvertException(ErrorCode.TARGET_PATH_NOT_EXIST);
        }
        if (!Files.isDirectory(targetDir)) {
            throw new GseinConvertException(ErrorCode.TARGET_PATH_NOT_DIRECTORY);
        }
        if (!Files.isWritable(targetDir)) {
            throw new GseinConvertException(ErrorCode.TARGET_PATH_NOT_WRITEABLE);
        }
        if (targetFormat == null) {
            throw new GseinConvertException(ErrorCode.TARGET_FORMAT_EMPTY);
        }

        for (Image sourceImage : sourceImages) {
            try {
                if (hasAlpha(sourceImage.getFormat()) && !hasAlpha(targetFormat)) {
                    alphaToNoAlpha(sourceImage);
                } else if (!hasAlpha(sourceImage.getFormat()) && hasAlpha(targetFormat)) {
                    noAlphaToAlpha(sourceImage);
                } else {
                    normalConvert(sourceImage);
                }
            } catch (IOException e) {
                throw new GseinConvertException(ErrorCode.CONVERT_FAILED);
            }
        }
    }


    private void normalConvert(Image sourceImage) throws IOException {
        BufferedImage newBufferedImage;
        BufferedImage bufferedImage = sourceImage.getBufferedImage();
        if (hasAlpha(targetFormat)) {
            newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(),
                    null);
        } else {
            newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(),
                    Color.WHITE, null);
        }

        writeToFile(sourceImage, newBufferedImage);

    }

    private void writeToFile(Image sourceImage, BufferedImage newBufferedImage) throws IOException {
        // 写入文件
        Files.createDirectories(targetDir);
        Path path = targetDir.resolve(sourceImage.getName());
        ImageIO.write(newBufferedImage, targetFormat.name().toLowerCase(), Files.newOutputStream(path));
    }

    private boolean hasAlpha(ImageFormat format) {
        return format == ImageFormat.PNG || format == ImageFormat.TIFF || format == ImageFormat.TGA;
    }

    private void alphaToNoAlpha(Image sourceImage) throws IOException {
        BufferedImage bufferedImage = sourceImage.getBufferedImage();
        BufferedImage newBufferedImage = new BufferedImage(
                bufferedImage.getWidth(), bufferedImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);

        // TYPE_INT_RGB:创建一个RBG图像，24位深度，成功将32位图转化成24位
        newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(),
                Color.WHITE, null);

        // 写入文件
        writeToFile(sourceImage, newBufferedImage);
    }

    private void noAlphaToAlpha(Image sourceImage) throws IOException {
        BufferedImage bufferedImage = sourceImage.getBufferedImage();
        BufferedImage newBufferedImage;
        if (writeToAlpha) {
            newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            FilteredImageSource imageSource = new FilteredImageSource(bufferedImage.getSource(), new CustomImageFilter());
            java.awt.Image image = Toolkit.getDefaultToolkit().createImage(imageSource);
            image.flush();

            newBufferedImage.createGraphics().drawImage(image, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), null);

        } else {
            newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(),
                    Color.WHITE, null);
        }

        // 写入文件
        writeToFile(sourceImage, newBufferedImage);
    }
}
