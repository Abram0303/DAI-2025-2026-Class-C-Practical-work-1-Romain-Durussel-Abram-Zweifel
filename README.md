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

### Usage Instructions
To use the command-line tool, follow these steps:

1. Compile the project :
```./mvnw clean package```


2. Run the desired command with the appropriate arguments. Example commands :
   - Grayscale conversion:
     ``` java -jar target/ImageProcessor-1.0-SNAPSHOT.jar -i image/input.jpg -o image/output.jpg grayscale```
   - Color inversion:
     ``` java -jar target/ImageProcessor-1.0-SNAPSHOT.jar -i image/input.jpg -o image/output.jpg invert```
   - Image rotation:
     ``` java -jar target/ImageProcessor-1.0-SNAPSHOT.jar -i image/input.jpg -o image/output.jpg rotate -a 90```
   - Help command:
     ``` java -jar target/ImageProcessor-1.0-SNAPSHOT.jar help```

---

### Repository Information

Repository : [link here](https://github.com/Abram0303/DAI-2025-2026-Class-C-Practical-work-1-Romain-Durussel-Abram-Zweifel#) 

Related commit hash : xxx
