package interfaces;

import logics.analyzer.*;
import logics.analyzer.Package;

import java.util.function.Function;

/**
 * Created by bedux on 03/03/16.
 */
public interface Analyser <T> {
        T analysis( Component value);
}
