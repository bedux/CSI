package logics.models.tools;

/**
 * Created by bedux on 10/03/16.
 */
public class Data{

    public String path = "";
    public int minNOM = 0;
    public int maxNOM = 0;

    public int minWC = 0;
    public int maxWC = 0;

    public int minNOL = 0;
    public int maxNOL= 0;

    public Long id = 0L;
    @Override
    public String toString() {
        return "Data{" +
                "path='" + path + '\'' +
                ", minNOM=" + minNOM +
                ", maxNOM=" + maxNOM +
                ", minWC=" + minWC +
                ", maxWC=" + maxWC +
                ", minNOL=" + minNOL +
                ", maxNOL=" + maxNOL +
                ", id=" + id +


                '}';
    }
}
