package interfaces;

import com.avaje.ebean.Expression;
import com.fasterxml.jackson.databind.node.ObjectNode;
import logics.models.tools.Data;

/**
 * Created by bedux on 10/03/16.
 */
public abstract class Filter<T> {
    protected static String[] parseRabge(String r) {
        r = r.replace("+", "");
        return r.split("-");
    }

    public abstract Expression getExpressionFromData(Data d);

    public abstract ObjectNode getDescription(Long id);

    public abstract boolean handleRequest(String[] request, Data d);

    public enum FilterType {
        RANGE("RANGE"), STRING("INPUT");
        private String value;

        FilterType(final String value) {
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

}
