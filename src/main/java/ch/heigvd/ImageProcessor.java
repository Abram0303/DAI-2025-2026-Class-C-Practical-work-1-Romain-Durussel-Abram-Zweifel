package ch.heigvd;

import ch.heigvd.commands.Grayscale;
import ch.heigvd.commands.Invert;
import ch.heigvd.commands.Rotate;

import java.io.File;
import picocli.CommandLine;

@CommandLine.Command(
        description = "A small CLI with subcommands to process images.",
        version = "1.0.0",
        showDefaultValues = true,
        subcommands = {
                Grayscale.class,
                Invert.class,
                Rotate.class
        },
        scope = CommandLine.ScopeType.INHERIT,
        mixinStandardHelpOptions = true
)

public class ImageProcessor {
    public static void main(String[] args)
    {
        System.out.println("Hello, World");
    }
}
