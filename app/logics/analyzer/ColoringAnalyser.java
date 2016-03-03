package logics.analyzer;

import interfaces.Analyser;
import interfaces.Component;
import logics.renderTools.Point3d;

/**
 * Created by bedux on 03/03/16.
 */
public class ColoringAnalyser implements Analyser<Point3d>{

    private int max = 0;
    public ColoringAnalyser(int max){
        this.max = max;
    }
    @Override
    public Point3d analysis(Component c) {

        c.getComponentList().stream().forEach((x) -> x.applyFunction((new ColoringAnalyser(max))::analysis));
        Point3d n =  new Point3d(0.0f,0.0f,0.0f);;
        if (c instanceof BinaryFile) {

        } else if (c instanceof DataFile) {
            n=analysisCast((DataFile)c);
        }else if (c instanceof Package){
            n=analysisCast((Package) c);
        }
        c.getFeatures().setColor(new float[]{n.getX(), n.getY(),n.getZ()});
        return n;
    }

    private Point3d analysisCast(Package p){

        float color = p.getFeatures().getRemotness()/((float)max);
        return new Point3d(0,color,color);
    }

    private Point3d analysisCast(DataFile p){
        return new Point3d((float)Math.random(),(float)Math.random(),(float)Math.random());
    }

}
