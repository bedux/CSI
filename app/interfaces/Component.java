package interfaces;

import logics.analyzer.Features;

import java.io.File;
import java.nio.file.Path;

/**
 * Created by bedux on 24/02/16.
 */
public interface Component {
    Features operation();
    Features operation(Features features);
    public boolean add(String s,Path f,String allPath);

    }
