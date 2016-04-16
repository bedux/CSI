package exception;

import com.github.javaparser.ParseException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * Created by bedux on 02/03/16.
 */
public class CustomException extends RuntimeException {

    private Exception e;
    private String message;

    public CustomException(Exception e) {
        e.printStackTrace();

        this.e = e;
    }
    public CustomException(SQLException e){
        e.printStackTrace();
        this.e = e;
    }

    public CustomException(IOException e){
         e.printStackTrace();
        this.e = e;
    }
    public CustomException(InstantiationException e){
        e.printStackTrace();
        this.e = e;
    }
    public CustomException(ParseException e){
        e.printStackTrace();
        this.e = e;
    }
    public CustomException(IllegalAccessException e){
        e.printStackTrace();
        this.e = e;
    }




    public CustomException() {

        System.err.println(Arrays.toString(Thread.currentThread().getStackTrace()));

    }

    public CustomException(String message) {
        this.message = message;
        System.err.println("######################");
        System.err.println(message);
        System.err.println("######################");

        System.err.println(Arrays.toString(Thread.currentThread().getStackTrace()));

    }

    public Exception getException() {
        return e;
    }
}
