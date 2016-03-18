package logics.models.tools;

/**
 * Created by bedux on 03/03/16.
 */
public class MaximumMinimumData {
    public  float maxDepth;
    public  float maxWidth;
    public  float maxHeight;
    
    public  float minDepth;
    public  float minWidth;
    public  float minHeight;

    public MaximumMinimumData(float width, float height, float depth) {
        this.maxDepth = depth;
        this.maxWidth = width;
        this.maxHeight = height;

        this.minHeight = height;
        this.minWidth = width;
        this.minDepth = depth;
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

