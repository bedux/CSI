package logics.pipeline.analayser;

import com.fasterxml.jackson.databind.JsonNode;
import interfaces.HandlerResult;

import java.util.List;

/**
 * Created by bedux on 08/03/16.
 */
public class AnalyserHandlerResult implements HandlerResult {
    public List<JsonNode> json;

    public AnalyserHandlerResult(List<JsonNode> json) {
        this.json = json;
    }
}
