## DAI 2025–2026 - Class C - Practical Work 1
#### By Romain Durussel & Abram Zweifel

---

### Project Overview

In this project, we developed a command-line tool to perform various image processing operations.
The tool allows users to easily apply transformations to RGB images directly from the terminal.

---

### Implemented Commands

1. ```grayscale```: converts an RGB color image to grayscale. 


2. ```invert``` program : inverts the colors of an RGB image.


3. ```rotate``` program : rotates the image by 90°, 180°, or 270° (clockwise).

---

### Build Instructions

Follow these explicit commands to clone and build the project on your own computer :

1. Clone the repository from GitHub :
``` git clone https://github.com/Abram0303/DAI-2025-2026-Class-C-Practical-work-1-Romain-Durussel-Abram-Zweifel.git```


2. Navigate to the project directory :
``` cd DAI-2025-2026-Class-C-Practical-work-1-Romain-Durussel-Abram-Zweifel```


3. Build the project using Maven (ensure you have installed it) :
```./mvnw clean package```

After this step, the generated JAR file will be located in the target/ directory :
``` target/ImageProcessor-1.0-SNAPSHOT.jar ```

---

### Usage Instructions

1. Open a terminal and navigate to the project directory.


2. Run the desired command with the appropriate arguments. Here are some examples (with inputs at your disposal in the image/input/ folder) :
   - Grayscale conversion:
     ``` java -jar target/ImageProcessor-1.0-SNAPSHOT.jar -i image/input/input1.jpg -o image/output/output1_grayscale.jpg grayscale```
   - Color inversion:
     ``` java -jar target/ImageProcessor-1.0-SNAPSHOT.jar -i image/input/input1.jpg -o image/output/output1_invert.jpg invert```
   - Image rotation:
     ``` java -jar target/ImageProcessor-1.0-SNAPSHOT.jar -i image/input/input1.jpg -o image/output/output1_rotate.jpg rotate -a 90```
   - Help command:
     ``` java -jar target/ImageProcessor-1.0-SNAPSHOT.jar -h help```
   - Version command:
     ``` java -jar target/ImageProcessor-1.0-SNAPSHOT.jar -V version```

---

### Use of AI Tools and External Sources

We used ChatGPT as an inspiration and support tool during the development of this project.
ChatGPT suggested the use of the Java classes ```BufferedImage``` and ```Raster``` for direct pixel-level image manipulation.

We decided to follow this approach because these classes are part of the Java standard image processing API and are specifically designed to handle pixel data.

- ```BufferedImage``` provides a convenient object representation of an image in memory, allowing access to its pixels and color model.

- ```Raster``` and ```WritableRaster``` allow direct reading and modification of pixel values (R, G, B), which is ideal for operations such as grayscale conversion or color inversion.

By contrast, classes like ```BufferedInputStream``` and ```BufferedOutputStream``` only handle raw binary streams (sequences of bytes).
They are useful for reading or writing files efficiently, but they do not interpret or manipulate image pixels.
Using them would require manually parsing the image format (e.g., BMP, PNG, JPG).

