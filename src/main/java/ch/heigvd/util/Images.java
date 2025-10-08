package ch.heigvd.util;

import picocli.CommandLine;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import ch.heigvd.IOOptions;

public class Images {

    @CommandLine.Mixin
    public static IOOptions io;

    public static BufferedImage readImage(){

        File input = io.inputFile;

        try {
            if(!input.exists()){
                throw new IllegalArgumentException("Input file not found: " + input);
            }

            return ImageIO.read(input);
        } catch(Exception e) {
            throw new RuntimeException("Failed to read image: " + input + " (" + e.getMessage() + ")", e);
        }
    }

    public static void writeImage(BufferedImage image){

        File output = io.outputFile;

        if (output == null) {
            output = new File("output.jpg");
        }

        try {

            String name = output.getName();
            int dot = name.lastIndexOf('.');
            String format = dot > 0 ? name.substring(dot + 1) : "jpg";


            if(!ImageIO.write(image, format, output)){
                throw new RuntimeException("No writer for format: " + format);
            };
        } catch(Exception e) {
            throw new RuntimeException("Failed to write image: " + output + " (" + e.getMessage() + ")", e);
        }
    }
}
