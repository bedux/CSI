package logics.models.json;

import logics.analyzer.Features;

import java.util.Arrays;

/**
 * Created by bedux on 26/02/16.
 */
public class RenderComponent {

    public float width;
    public float height;
    public float deep;

    public float NOM=0;
    public float WC = 0;
    public float NOA = 0;
    public long size = 0;



    public float[] color;
    public int segment;
    public String id;

    public RenderComponent(float width, float height,float deep, float[] color, int segment, String id, RenderChild[] children) {
      inizialize(width,height,deep,color,segment,id,children);
    }
    public RenderComponent(Features f, RenderChild[] children) {
        inizialize(f.getRendererWidth(), f.getHeight(), f.getRendererDeep(), f.getColor(), f.getSegment(), f.getPath(), children);
        NOM = f.getMethodsNumber();
        WC = f.getWordCount();
        size = f.getSize();
    }

    private void inizialize(float width, float height,float deep, float[] color, int segment, String id, RenderChild[] children){
        this.width = width;
        this.height = height;
        this.color = color;
        this.segment = segment;
        this.id = id;
        this.children = children;
        this.deep = deep;
    }

        public RenderChild[] children;

    @Override
    public String toString() {
        return "RenderComponent{" +
                "children=" + Arrays.toString(children) +
                ", height=" + height +
                ", color=" + Arrays.toString(color) +
                ", segment=" + segment +
                ", id='" + id + '\'' +
                ", width=" + width +
                '}';
    }
}

