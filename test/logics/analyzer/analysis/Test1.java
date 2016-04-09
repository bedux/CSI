package logics.analyzer.analysis;

/**
 * Created by bedux on 31/03/16.
 */
public class Test1 {


     final class FinalClass{
         /***
          * sd
          */
        private void GetFinalStuff(){

        }
    }

    class NormalNestedClass{
        /***
         * sd sdlkjfsdf
         * @param l asdasd
         */
        private void GetNestedStuff(int l){
                int a = Integer.bitCount(3);
        }
    }

    static class StaticClass{
        /***
         * sd
         */
        private void GetStaticStuff(){

        }
    }


    interface  Foo{
        /***
         * sdasdasjkdhasjd
         *
         */
         void GetStaticStuff();
    }





}
