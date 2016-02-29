import interfaces.SplitCase;
import logics.renderTools.BoundingBox;
import logics.renderTools.splitCases.SplitCaseTR;
import logics.renderTools.splitCases.SplitCaseTT;
import org.junit.Test;

import javax.validation.constraints.AssertTrue;

import static junit.framework.Assert.assertTrue;

/**
 * Created by bedux on 28/02/16.
 */
public class TestSplit {
    @Test
    public void testTT(){
        BoundingBox source = new BoundingBox(10,10, 50,50);
        BoundingBox target = new BoundingBox(0,0, 80,20);
        SplitCaseTT sp = new SplitCaseTT();
        assertTrue(sp.split(source, target).length > 0);

         source = new BoundingBox(0,0, 50,50);
         target = new BoundingBox(10,0, 10,10);
        assertTrue(sp.split(source, target).length == 0);
    }


}
