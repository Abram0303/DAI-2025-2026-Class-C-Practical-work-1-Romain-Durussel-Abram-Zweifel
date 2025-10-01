package ch.heigvd.commands;

import ch.heigvd.ImageProcessor;
import picocli.CommandLine;

@CommandLine.Command(name = "invert", description = "Invert the color in the image.")

public class Invert {

    @CommandLine.ParentCommand protected ImageProcessor parent;

}
