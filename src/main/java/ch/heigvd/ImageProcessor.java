package ch.heigvd;

import ch.heigvd.commands.Grayscale;
import ch.heigvd.commands.Invert;
import ch.heigvd.commands.Rotate;

import picocli.CommandLine;

@CommandLine.Command(
        description = "A small CLI with subcommands to process images.",
        version = "1.1",
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

    @CommandLine.Mixin IOOptions io;

    public static void main(String[] args)
    {
        int exitCode = new CommandLine(new ImageProcessor()).execute(args);
        System.exit(exitCode);
    }
}
