# Webcam-Based-Painting-Program

### Overview
This project implements a webcam-based painting application where a portion of the live webcam image (such as a hand or the tip of a colored marker) acts as a virtual paintbrush. The system identifies and tracks this paintbrush in real-time based on its color and enables users to draw on a digital canvas using their movements.This project was co-authored by me and Torsha Chakraverty - with me doing the RegionFinder and her doing the CamPaint programs respectively. 

### How It Works
The core challenge is identifying the "paintbrush" object in the webcam feed. We use a region growing algorithm, also known as flood fill, to detect uniformly colored regions in the image that match a target color selected by the user.

### Steps:
#### Color Selection:
The user clicks on a pixel in the webcam image to specify a target color. This pixel should ideally belong to the paintbrush (e.g., a colored marker tip).

#### Region Growing (Flood Fill):
Starting from the selected pixel, the algorithm:

1. Adds that pixel to a region.

2. Examines its 8-connected neighbors (and itself, if within bounds).

3. Adds any neighbor with a color approximately matching the target color.

4. Recursively expands to their neighbors.

5. Stops when no new matching pixels are found.

#### Paintbrush Detection:
Multiple regions may match the color. The largest connected region is assumed to be the paintbrush.

#### Drawing:
The center or tip of the largest region is tracked over time, and its motion is used to draw on a virtual canvas.

#### Features
1. Real-time tracking of a colored object via webcam.

2. Color-based region detection using flood fill.

3. Mouse-based target color selection.

