package interfaces;

import logics.renderTools.BoundingBox;

/**
 * Created by bedux on 28/02/16.
 */
public interface SplitCase {

    BoundingBox[] split(BoundingBox source,BoundingBox target);
}
