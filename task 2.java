import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

public class ImageEncryptionTool {

    private static final int SWAP_DISTANCE = 5; 

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java ImageEncryptionTool <encrypt/decrypt> <input_image> <output_image>");
            return;
        }

        String mode = args[0].toLowerCase();
        String inputImagePath = args[1];
        String outputImagePath = args[2];

        if (!mode.equals("encrypt") && !mode.equals("decrypt")) {
            System.out.println("Invalid mode. Use 'encrypt' or 'decrypt'.");
            return;
        }

        try {
            File inputFile = new File(inputImagePath);
            if (!inputFile.exists()) {
                System.out.println("Input file does not exist.");
                return;
            }

            BufferedImage image = ImageIO.read(inputFile);
            BufferedImage resultImage = processImage(image, mode.equals("encrypt"));

            File outputFile = new File(outputImagePath);
            ImageIO.write(resultImage, "png", outputFile);
            System.out.println("Image " + mode + "ed successfully!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage processImage(BufferedImage image, boolean encrypt) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage resultImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int currentIndex = y * width + x;
                int swapIndex = (currentIndex + SWAP_DISTANCE) % (width * height);

                int currentPixel = image.getRGB(x, y);
                int swapX = swapIndex % width;
                int swapY = swapIndex / width;

                if (encrypt || currentIndex < swapIndex) {
                    int swapPixel = image.getRGB(swapX, swapY);
                    resultImage.setRGB(x, y, swapPixel);
                    resultImage.setRGB(swapX, swapY, currentPixel);
                } else {
                    resultImage.setRGB(x, y, currentPixel);
                }
            }
        }

        return resultImage;
    }
}
