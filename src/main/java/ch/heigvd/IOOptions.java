package ch.heigvd;

import picocli.CommandLine;
import java.io.File;

public class IOOptions {
    @CommandLine.Option(names={"-i","--input"}, required=true)
    public File inputFile;

    @CommandLine.Option(names={"-o","--output"}, required=false, defaultValue = "image/output.jpg")
    public File outputFile;
}

