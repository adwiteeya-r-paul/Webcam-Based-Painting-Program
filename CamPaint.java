import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Code for PS-1 "CamPaint"
 * Webcam-based drawing
 *
 * @author Adwiteeya Rupantee Paul & Torsha Chakraverty, Dartmouth CS10, Spring 2024
 */
public class CamPaint extends VideoGUI {
    private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
    private RegionFinder finder;			// handles the finding
    private Color targetColor;          	// color of regions of interest (set by mouse press)
    private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
    private BufferedImage painting;			// the resulting masterpiece

    /**
     * Initializes the region finder and the drawing
     */
    public CamPaint() {
        finder = new RegionFinder();
        clearPainting();
    }

    /**
     * Resets the painting to a blank image
     */
    protected void clearPainting() {
        painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * VideoGUI method, here drawing one of live webcam, recolored image, or painting,
     * depending on display variable ('w', 'r', or 'p')
     */
    @Override
    public void handleImage() {
        if (targetColor != null) {             //checks if target color set is null
            finder.setImage(image);          //initializes value of image
            finder.findRegions(targetColor); //finds regions of target color in image
            finder.recolorImage();           //recolors image
            ArrayList<Point> largest = finder.largestRegion();
            if (largest != null) {
                for (Point p: largest) {     //iterates through the points in the largest region
                    painting.setRGB(p.x, p.y, paintColor.getRGB());   //makes the brush
                }
            }
        }
        if (displayMode == 'w') {              //if display mode is 'w'
            setImage1(image);
        } else if (displayMode == 'r') {       //if display more is 'r'
            setImage1(finder.getRecoloredImage());
        } else if (displayMode == 'p') {       //if display mode is 'p'
            setImage1(painting);
        }

    }


    /**
     * Overrides the Webcam method to set the track color.
     */
    @Override
    public void handleMousePress(int x, int y) {
        if (image != null) {                   //checks if image is null
            targetColor = new Color(image.getRGB(x, y));   //sets target color
        }

    }

    /**
     * Webcam method, here doing various drawing commands
     */
    @Override
    public void handleKeyPress(char k) {
        if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
            displayMode = k;
        }
        else if (k == 'c') { // clear
            clearPainting();
        }
        else if (k == 'o') { // save the recolored image
            ImageIOLibrary.saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
        }
        else if (k == 's') { // save the painting
            ImageIOLibrary.saveImage(painting, "pictures/painting.png", "png");
        }
        else {
            System.out.println("unexpected key "+k);
        }
    }

    public static void main(String[] args) {
        new CamPaint();
    }
}