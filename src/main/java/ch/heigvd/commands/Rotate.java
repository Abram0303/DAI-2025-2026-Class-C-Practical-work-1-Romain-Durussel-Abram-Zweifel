package ch.heigvd.commands;


import ch.heigvd.ImageProcessor;
import ch.heigvd.IOOptions;
import picocli.CommandLine;

@CommandLine.Command(
        name = "rotate",
        description = "Rotate an image by 90, 180, or 270 degrees."
)

public class Rotate {

    @CommandLine.Mixin IOOptions io;

    @CommandLine.Option(
            names = {"-a", "--angle"},
            description = "Rotation angle (valid: 90, 180, 270).",
            defaultValue = "90"
    )
    public int angle;

    @CommandLine.ParentCommand protected ImageProcessor parent;

}
