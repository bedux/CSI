package logics.models.tools;

/**
 * Created by bedux on 10/03/16.
 */
public class Data{

    public enum ActionList {HIDE("Hide"),SHOW("Show"),SHOWOTHER("ShowOther"),HIDEOTHER("HideOther");
        private String value;

        ActionList(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.getValue();
        }
    }
    public String path = "";
    public int minNOM = 0;
    public int maxNOM = 0;

    public int minWC = 0;
    public int maxWC = 0;

    public int minNOL = 0;
    public int maxNOL= 0;

    public Long id = 0L;

    public ActionList actionList=ActionList.SHOW;


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
