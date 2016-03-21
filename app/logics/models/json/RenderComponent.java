package logics.models.json;

import logics.analyzer.DataFeatures;

import java.util.Arrays;

/**
 * Created by bedux on 26/02/16.
 */
public class RenderComponent  {

    public float width;
    public float height;
    public float deep;

    public DataFeatures features;

    public long size = 0;



    public float color;
    public int buildingType;
    public String id;

    public RenderComponent(float width, float height,float deep, float color, int buildingType, String id, RenderChild[] children) {
      inizialize(width,height,deep,color, buildingType,id,children);
    }
    public RenderComponent(DataFeatures f, RenderChild[] children) {
        inizialize(f.getRendererWidth(), f.getHeight(), f.getRendererDeep(), f.getColor(), f.getBuildingType(), f.getPath(), children);

        features = f;
    }

    private void inizialize(float width, float height,float deep, float color, int segment, String id, RenderChild[] children){
        this.width = width;
        this.height = height;
        this.color = color;
        this.buildingType = segment;
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
                ", color=" + (color) +
                ", buildingType=" + buildingType +
                ", id='" + id + '\'' +
                ", width=" + width +
                '}';
    }
}

