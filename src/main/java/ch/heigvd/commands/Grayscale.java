package ch.heigvd.commands;

import ch.heigvd.ImageProcessor;
import picocli.CommandLine;

@CommandLine.Command(name = "grayscale", description = "Convert an image to grayscale.")

public class Grayscale {

    @CommandLine.ParentCommand protected ImageProcessor parent;


}

/*
package ch.heigvd.commands;

import ch.heigvd.Main;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(name = "goodbye", description = "Print a 'Goodbye World!' type of message.")
public class Goodbye implements Callable<Integer> {

    @CommandLine.ParentCommand protected Main parent;

    @CommandLine.Option(
            names = {"-f", "--farewells"},
            description = "The farewells to address the user.",
            defaultValue = "Goodbye")
    protected String farewells;

    @Override
    public Integer call() {
        System.out.println(farewells + " " + parent.getName() + "!");
        return 0;
    }
}
*/