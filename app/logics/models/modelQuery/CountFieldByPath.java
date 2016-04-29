package logics.models.modelQuery;

/**
 * Created by bedux on 29/04/16.
 */
public class CountFieldByPath implements IQuery<String,Long> {
    @Override
    public Long execute(String param) {

        return (long)new AllJavaFieldsFormPath().execute(param).size();
    }

    @Override
    public IQuery<String, Long> clone() {
        return new CountFieldByPath();
    }
}
