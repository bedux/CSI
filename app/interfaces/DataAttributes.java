package interfaces;


/**
 * Created by bedux on 17/03/16.
 */
public interface DataAttributes {


  enum DataName {
      NoMethod("noMethod"), NoPrivateMethod("noPrivateMethod"), NoPublicMethod("noPublicMethod"), NoProtectedMethod("noProtectedMethod"),
      NOF("noF"), //number of fields
      NoForSTM("noForSTM"), //number of for loop
      NoForeachSTM("noForeachSTM"), //number of for loop
      NoWhile("noWhile"), //number of foreach loop
      NoIf("noIf"),
      NoWord("noWord"), //number of foreach loop
      size("size"),
      NoLine("noLine");
      private String value;
      DataName(final String value) {
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


    public int getNoMethod();

    public void setNoMethod(int noMethod);

    public int getNoPrivateMethod();

    public void setNoPrivateMethod(int noPrivateMethod) ;

    public int getNoPublicMethod();

    public void setNoPublicMethod(int noPublicMethod);

    public int getNoProtectedMethod();

    public void setNoProtectedMethod(int noProtectedMethod);

    public int getNoF();

    public void setNoF(int NOF);

    public int getNoForSTM();

    public void setNoForSTM(int noForSTM);

    public int getNoForeachSTM();

    public void setNoForeachSTM(int noForeachSTM);

    public int getNoWhile();

    public void setNoWhile(int noWhile);

    public int getNoIf();

    public void setNoIf(int noIf);

    public int getNoWord();

    public void setNoWord(int noWord);

    public int getSize();

    public void setSize(int size);

    public int getNoLine();

    public void setNoLine(int noLine);

}

