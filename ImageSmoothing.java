import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ImageSmoothing {
    
    // Method to load the image and convert it to a 2D array of pixel values
    public static int[][] loadImageAsMatrix(String filePath) {
        try {
            BufferedImage image = ImageIO.read(new File(filePath));
            int width = image.getWidth();
            int height = image.getHeight();
            int[][] imageMatrix = new int[height][width];

            // Store pixel values in 2D array
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    imageMatrix[y][x] = image.getRGB(x, y); // Get RGB value
                }
            }
            return imageMatrix;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to apply smoothing (average filter) on the image
    public static int[][] smoothImage(int[][] imageMatrix) {
        int height = imageMatrix.length;
        int width = imageMatrix[0].length;
        int[][] smoothedMatrix = new int[height][width];

        // Loop through each pixel and apply 3x3 average filter
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int sumR = 0, sumG = 0, sumB = 0;
                // Loop through 3x3 neighborhood
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dx = -1; dx <= 1; dx++) {
                        int pixel = imageMatrix[y + dy][x + dx];
                        // Extract the RGB components
                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = pixel & 0xFF;
                        sumR += r;
                        sumG += g;
                        sumB += b;
                    }
                }
                // Compute average for each channel and set new pixel
                int avgR = sumR / 9;
                int avgG = sumG / 9;
                int avgB = sumB / 9;
                smoothedMatrix[y][x] = (avgR << 16) | (avgG << 8) | avgB;
            }
        }
        return smoothedMatrix;
    }

    // Method to save the smoothed image to a file
    public static void saveImage(int[][] imageMatrix, String outputPath) {
        try {
            int height = imageMatrix.length;
            int width = imageMatrix[0].length;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // Set pixel values to the BufferedImage
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    image.setRGB(x, y, imageMatrix[y][x]);
                }
            }
            // Save the image
            ImageIO.write(image, "jpg", new File(outputPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Example usage
        String inputImagePath = "path_to_your_image.jpg";
        String outputImagePath = "path_to_output_image.jpg";
        
        // Load image as 2D matrix
        int[][] imageMatrix = loadImageAsMatrix(inputImagePath);
        
        if (imageMatrix != null) {
            // Smooth the image
            int[][] smoothedImage = smoothImage(imageMatrix);

            // Save the smoothed image
            saveImage(smoothedImage, outputImagePath);
        }
    }
}