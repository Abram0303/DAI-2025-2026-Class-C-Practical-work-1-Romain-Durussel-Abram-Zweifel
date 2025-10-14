package ch.heigvd;

import ch.heigvd.commands.Grayscale;
import ch.heigvd.commands.Invert;
import ch.heigvd.commands.Rotate;

import picocli.CommandLine;

@CommandLine.Command(
        description = "A small CLI with subcommands to process images.",
        version = "1.3", // Fully operational CLI while respecting the constraints
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

    @CommandLine.Mixin
    public IOOptions io;

    public static void main(String[] args)
    {
        int exitCode = new CommandLine(new ImageProcessor()).execute(args);
        System.exit(exitCode);
    }
}
