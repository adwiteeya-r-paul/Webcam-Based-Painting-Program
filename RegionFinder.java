import java.awt.*;
import java.awt.image.*;
import java.util.*;


/**
 * Code for PS-1 "RegionFinder
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 *
 *  @author Adwiteeya Rupantee Paul & Torsha Chakraverty, Dartmouth CS10, Spring 2024
 */
public class RegionFinder {
    private static final int maxColorDiff = 20;                // how similar a pixel color must be to the target color, to belong to a region

    private static final int minRegion = 50;                // how many points in a region to be worth considering

    private BufferedImage image;                            // the image in which to find regions
    private BufferedImage recoloredImage;                   // the image with identified regions recolored

    private ArrayList<ArrayList<Point>> regions;            // a region is a list of points

    /* so the identified regions are in a list of lists of points */

    public RegionFinder() {
        this.image = image;
    }

    public RegionFinder(BufferedImage image) {
        this.image = image;
    }


    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getRecoloredImage() {
        return recoloredImage;
    }

    /**
     * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
     */


    public void findRegions(Color targetColor) {
        int radius = 1;
        regions = new ArrayList<ArrayList<Point>>();
        BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {  //Loop over all the pixels

                Color c = new Color(image.getRGB(x, y));
                if (visited.getRGB(x, y) == 0 && colorMatch(c, targetColor)) { //If a pixel is unvisited and of the correct color
                    ArrayList<Point> region = new ArrayList<Point>(); //start a new region
                    ArrayList<Point> visit = new ArrayList<Point>(); //keep track of which pixels need to be visited, //initially just one
                    visit.add(new Point(x, y));
                    while (!visit.isEmpty()) { //As long as there's some pixel that needs to be visited,
                        Point point = visit.remove(0); //get one to visit
                        region.add(point); //add it to region
                        visited.setRGB(point.x, point.y, 1); //mark it as visited
                        System.out.println(visit.size());
                        for (int ny = Math.max(0, point.y - radius); ny < Math.min(image.getHeight(), point.y + 1 + radius); ny++) {
                            for (int nx = Math.max(0, point.x - radius); nx < Math.min(image.getWidth(), point.x + 1 + radius); nx++) { //loop over its neighbors
                                //System.out.println(visited.getRGB(nx, ny) == 0);
                                if (visited.getRGB(nx, ny) == 0 && colorMatch(targetColor, new Color(image.getRGB(nx, ny)))) { //if neighbor is of correct color
                                    visit.add(new Point(nx, ny)); //to be visited;
//									System.out.println(region);
                                    visited.setRGB(nx, ny, 1); //mark it as visited
                                }
                            }
                        }
                    }

                    System.out.println(region.size());
                    if 	(region.size() >= minRegion) {
                        regions.add(region);
                        //System.out.println("added region ... + region)
                    }
                    //System.out.println( "didn't add region + region (to small, size = ) = region.size());
                }
            }
        }
    }

    /**
     * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
     */
    protected static boolean colorMatch(Color c1, Color c2) {
        boolean match = false;
        if (Math.abs((c1.getRed()) - c2.getRed()) <= maxColorDiff &&
                Math.abs(c1.getGreen() - c2.getGreen()) <= maxColorDiff &&
                Math.abs(c1.getBlue() - c2.getBlue()) <= maxColorDiff) {
            match = true;
        }

        return match;
    }


    /**
     * Returns the largest region detected (if any region has been detected)
     */



    public ArrayList<Point> largestRegion() {
        if (regions==null || regions.isEmpty()) {
            return null;
        }
        ArrayList<Point> largestRegion=new ArrayList<>();
        for (ArrayList<Point> region: regions) {
            if (region.size()>largestRegion.size()) {
                largestRegion=region;
            }
        }
        return largestRegion;
    }

    /**
     * Sets recoloredImage to be a copy of image,
     * but with each region a uniform random color,
     * so we can see where they are
     */
    public void recolorImage() {
        // First copy the original
        recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
        // Now recolor the regions in it
        if (regions == null) {
            return;
        }

        for (ArrayList<Point> region : regions){
            Random randomNumber = new Random();
            Color randomColor = new Color(randomNumber.nextInt(256), randomNumber.nextInt(256), randomNumber.nextInt(256));
            for (Point point : region) {
                recoloredImage.setRGB(point.x, point.y, randomColor.getRGB());
            }
        }

        //

    }
}
