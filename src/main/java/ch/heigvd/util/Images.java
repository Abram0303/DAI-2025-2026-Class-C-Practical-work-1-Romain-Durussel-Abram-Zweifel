package ch.heigvd.util;

import picocli.CommandLine;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import ch.heigvd.IOOptions;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public class Images {

    @CommandLine.Mixin
    public static IOOptions io;

    public static BufferedImage readImage() {
        File input = io.inputFile;

        try {
            if (input == null) {
                throw new IllegalArgumentException("Input file is null.");
            }
            if (!input.exists()) {
                throw new IllegalArgumentException("Input file not found: " + input);
            }

            try (InputStream fis = new FileInputStream(input);
                 BufferedInputStream bis = new BufferedInputStream(fis)) {

                BufferedImage img = ImageIO.read(bis);
                if (img == null) {
                    throw new IllegalArgumentException("Unsupported or corrupt image: " + input);
                }
                return img;
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image: " + input + " (" + e.getMessage() + ")", e);
        }
    }

    public static void writeImage(BufferedImage image) {
        File output = io.outputFile;

        try {
            if (output == null) {
                throw new IllegalArgumentException("Output file is null.");
            }

            String name = output.getName();
            int dot = name.lastIndexOf('.');
            String format = (dot > 0) ? name.substring(dot + 1).toLowerCase() : null;

            try (OutputStream fos = new FileOutputStream(output);
                 BufferedOutputStream bos = new BufferedOutputStream(fos)) {

                if (!ImageIO.write(image, format, bos)) {
                    throw new RuntimeException("No writer for format: " + format);
                }
                bos.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write image: " + output + " (" + e.getMessage() + ")", e);
        }
    }
}
