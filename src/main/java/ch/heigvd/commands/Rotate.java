package ch.heigvd.commands;


import ch.heigvd.ImageProcessor;
import ch.heigvd.IOOptions;
import ch.heigvd.util.Images;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "rotate",
        description = "Rotate an image by 90, 180, or 270 degrees."
)

public class Rotate implements Callable<Integer> {

    @CommandLine.Option(
            names = {"-a", "--angle"},
            description = "Rotation angle (valid: 90, 180, 270).",
            defaultValue = "90"
    )
    public int angle;

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
