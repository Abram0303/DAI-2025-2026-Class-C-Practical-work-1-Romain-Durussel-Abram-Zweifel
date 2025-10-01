package ch.heigvd.commands;


import ch.heigvd.ImageProcessor;
import picocli.CommandLine;

@CommandLine.Command(name = "rotate", description = "Rotate an image a quarter turn.")

public class Rotate {

    @CommandLine.ParentCommand protected ImageProcessor parent;

}
