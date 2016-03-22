package logics.models.tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bedux on 03/03/16.
 */
public class MaximumMinimumData {
    public  float maxDepth;
    public  float maxWidth;
    public  float maxHeight;
    public  float maxColor;


    public  float minDepth;
    public  float minWidth;
    public  float minHeight;
    public  float minColor;


    public List<Float> getDepths() {
        return depths;
    }

    public List<Float> getHeights() {
        return heights;
    }

    public List<Float> getWidths() {
        return widths;
    }

    public List<Float> getColors() {
        return colors;
    }

    private List<Float> depths = new ArrayList<>();
    private List<Float> heights = new ArrayList<>();
    private List<Float> widths = new ArrayList<>();
    private List<Float> colors = new ArrayList<>();


    public void addSizes(float depth,float height,float width,float color){
        depths.add(depth);
        heights.add(height);
        widths.add(width);
        colors.add(color);
    }
    public void merge(List<Float> depth,List<Float> height,List<Float> width,List<Float> color){
        depths.addAll(depth);
        heights.addAll(height);
        widths.addAll(width);
        colors.addAll(color);
    }

    public MaximumMinimumData(float width, float height, float depth,float color) {
        this.maxDepth = depth;
        this.maxWidth = width;
        this.maxHeight = height;
        this.maxColor = color;

        this.minHeight = height;
        this.minWidth = width;
        this.minDepth = depth;
        this.minColor = color;

        addSizes(depth,height,width,color);

    }
    public void setOnlyIfMaxMinDepth(float wc){
        if(this.maxDepth <wc)this.maxDepth =wc;
        if(this.minDepth>wc)this.minDepth=wc;

    }
    public void setOnlyIfMaxMinWidth(float NOM){
        if(this.maxWidth<NOM)this.maxWidth=NOM;
        if(this.minWidth>NOM)this.minWidth=NOM;


    }
    public void setOnlyIfMaxMinHeight(float size){
        if(this.maxHeight<size)this.maxHeight=size;
        if(this.minHeight>size)this.minHeight=size;

    }
    public void setOnlyIfMaxMinColor(float color){
        if(this.maxColor<color)this.maxColor=color;
        if(this.minColor>color)this.minColor=color;

    }


    private float[] getDivision(int n, List<Float> array){
        float[] result = new float[n];
        array.sort(Float::compare);
        int step = array.size()/n;
        for(int i = 1;i<=n;i++){
            if(step*i< array.size()){
                result[i-1] = array.get(step*i);
            }else{
                result[i-1] = array.size()-1;
            }

        }
        return result;
    }

    public float[] getWidthDivision(int n){
        return getDivision(n,widths);
    }
    public float[] getHeightDivision(int n){
        return getDivision(n,heights);
    }
    public float[] getDepthDivision(int n){
        return getDivision(n,depths);
    }

    @Override
    public String toString() {
        return "MaximumMinimumData{" +
                "maxDepyh=" + maxDepth +
                ", maxWidth=" + maxWidth +
                ", maxHeight" + maxHeight +
                ", minDepth=" + minDepth +
                ", minWidth=" + minWidth +
                ", minHeight" + minHeight +
                '}';
    }
}

