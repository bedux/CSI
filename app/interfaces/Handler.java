package interfaces;

/**
 * Created by bedux on 07/03/16.
 */
public interface Handler <T extends HandlerParam,T1 extends HandlerResult>{

     T1 process(T param);
}
