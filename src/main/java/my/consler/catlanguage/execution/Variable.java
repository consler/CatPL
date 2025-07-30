package my.consler.catlanguage.execution;

import java.util.HashMap;
import java.util.Map;

public class Variable
{
    private static final Map<String, Object> variables = new HashMap<>();

    public static void setVariable(String name, Object value)
    {
        variables.put(name, value);

    }

    public static String getVariable(String name)
    {
        return String.valueOf( variables.get(name));

    }
}
