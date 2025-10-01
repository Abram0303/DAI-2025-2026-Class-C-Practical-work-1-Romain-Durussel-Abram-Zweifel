package ch.heigvd.commands;

import ch.heigvd.ImageProcessor;
import ch.heigvd.IOOptions;
import picocli.CommandLine;

@CommandLine.Command(name = "invert", description = "Invert the color in the image.")

public class Invert {

    @CommandLine.Mixin IOOptions io;

    @CommandLine.ParentCommand protected ImageProcessor parent;

}
