package consler.catlanguage.execution;

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
        if (variables.containsKey(name))
        {
            return String.valueOf( variables.get(name));

        }
        else
        {
            throw new RuntimeException("Not a variable: " + name);

        }

    }
}
