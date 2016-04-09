package logics.models.db.information;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bedux on 25/03/16.
 */
public class MethodInfoJSON {
    public String modifier = "";
    public String signature = "";
    public int lineStart = 0;
    public int lineEnd = 0;
    public List<String> variableDeclaration= new ArrayList<>();

}

