package ch.heigvd.commands;

import ch.heigvd.ImageProcessor;
import ch.heigvd.IOOptions;
import ch.heigvd.util.Images;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "grayscale", description = "Convert an image to grayscale.")

public class Grayscale implements Callable<Integer> {

    @CommandLine.ParentCommand protected ImageProcessor parent;

    public Integer call(){

        try{
            Images.io = parent.io;

            return 0;
        } catch (Exception e) {
            return 1;
        }
    }
}