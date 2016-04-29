package logics.models.modelQuery;

/**
 * Created by bedux on 29/04/16.
 */
public class CountMethodByPath implements IQuery<String,Long> {
    @Override
    public Long execute(String param) {

        return (long)new AllJavaMethodFormPath().execute(param).size();
    }

    @Override
    public IQuery<String, Long> clone() {
        return new CountMethodByPath();
    }
}
