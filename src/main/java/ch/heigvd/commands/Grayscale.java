package ch.heigvd.commands;

import ch.heigvd.ImageProcessor;
import ch.heigvd.IOOptions;
import picocli.CommandLine;

@CommandLine.Command(name = "grayscale", description = "Convert an image to grayscale.")

public class Grayscale {

    @CommandLine.Mixin IOOptions io;

    @CommandLine.ParentCommand protected ImageProcessor parent;


}