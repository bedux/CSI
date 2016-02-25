package interfaces;

import logics.analyzer.Features;

import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by bedux on 24/02/16.
 */
public interface Component {
    Features operation();
    Features operation(Features features);
    boolean add(String s,Path f,String allPath);

    Features getFeatures();
    //Compute the statistics independently from level to level
    void applyIndependent(Consumer<Component> function);
}
