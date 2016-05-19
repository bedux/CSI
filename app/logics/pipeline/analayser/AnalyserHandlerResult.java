package logics.pipeline.analayser;

import com.fasterxml.jackson.databind.JsonNode;
import interfaces.HandlerResult;

/**
 * Created by bedux on 08/03/16.
 */
public class AnalyserHandlerResult implements HandlerResult {
    public JsonNode json;

    public AnalyserHandlerResult(JsonNode json) {
        this.json = json;
    }
}
