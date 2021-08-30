package verteilte_systeme;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessing {

	/**
	 * Converts the input RGB image to a single-channel gray scale array.
	 * 
	 * @param img The input RGB image
	 * @return A 2-D array with intensities
	 */
	private static int[][] convertToGrayScaleArray(BufferedImage img) {
	    int[][] out = new int[img.getWidth()][img.getHeight()];
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int r = img.getRGB(x, y) >> 16 	& 0xff;
				int g = img.getRGB(x, y) >> 8 	& 0xff;
				int b = img.getRGB(x, y) >> 0 	& 0xff;
				out[x][y] = (int)(0.299 * r + 0.587 * g + 0.114 * b);
			}
		}

		return out;
	}

	/**
	 * Converts a single-channel (gray scale) array to an RGB image.
	 * 
	 * @param img
	 * @return BufferedImage
	 */
	private static BufferedImage convertToBufferedImage(int[][] img) {
		BufferedImage out = new BufferedImage(img.length, img[0].length, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < img.length; x++) {
			for (int y = 0; y < img[0].length; y++) {
				int g = img[x][y];
				int c = g << 0 | g << 8 | g << 16;
				out.setRGB(x, y, c);
			}
		}

		return out;
	}

	/**
	 * Converts input image to gray scale and applies the kernel.
	 * 
	 * @param img    RGB input image
	 * @param kernel
	 * @return convolved gray-scale image
	 */
	private static BufferedImage filter(BufferedImage img, Kernel kernel) {
		int[][] grayscale = convertToGrayScaleArray(img);
		int[][] convolved = kernel.convolve(grayscale);
		return convertToBufferedImage(convolved);
	}

	public static void main(String[] args) throws IOException  {
		BufferedImage image = ImageIO.read(new File("Aufgaben/Verteilte_Systeme/example.jpg"));
		ImageIO.write(filter(image, Kernels.GaussianBlur5x5()), "jpg", new File("Aufgaben/Verteilte_Systeme/gauss5x5.jpg"));
		ImageIO.write(filter(image, Kernels.BoxBlur3x3()), "jpg", new File("Aufgaben/Verteilte_Systeme/box3x3.jpg"));
		ImageIO.write(filter(image, Kernels.EdgeDetection()), "jpg", new File("Aufgaben/Verteilte_Systeme/edge.jpg"));
		ImageIO.write(filter(image, Kernels.Sharpen()), "jpg", new File("Aufgaben/Verteilte_Systeme/sharpen.jpg"));
		ImageIO.write(filter(image, Kernels.Identity()), "jpg", new File("Aufgaben/Verteilte_Systeme/identity.jpg"));
		ImageIO.write(filter(image, Kernels.Relief()), "jpg", new File("Aufgaben/Verteilte_Systeme/relief.jpg"));
		ImageIO.write(filter(image, Kernels.MotionBlur()), "jpg", new File("Aufgaben/Verteilte_Systeme/motionblur.jpg"));
	}
}
