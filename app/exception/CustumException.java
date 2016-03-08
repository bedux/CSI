package exception;

/**
 * Created by bedux on 02/03/16.
 */
public class  CustumException extends RuntimeException {

    private Exception e;
    public  CustumException(Exception e){
        this.e = e;
    }
    public  CustumException(){
    }
    public  Exception getException(){
        return e;
    }
}
