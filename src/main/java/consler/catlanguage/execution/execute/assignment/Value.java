package consler.catlanguage.execution.execute.assignment;

import consler.catlanguage.parser.ParsingError;
import consler.catlanguage.parser.RuntimeError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Value
{
    private static final Map<String, Object> identifiers = new HashMap<>();

    public static void setIdentifier(String name, Object value)
    {
        identifiers.put(name, value);
    }
    @SuppressWarnings("unchecked")
    public static void setIdentifier(String name, Object index, Object value)
    {
        if(identifiers.get(name) instanceof HashMap<?,?>)
            ((HashMap<Object, Object>) identifiers.get(name)).put(index, value);
        else
        {
            identifiers.put(name, new HashMap<>());
            ((HashMap<Object, Object>) identifiers.get(name)).put(index, value);
        }
    }


    public static Object getIdentifier(String name)
    {
        if (!identifiers.containsKey(name))
            throw new RuntimeException("Not an identifier: " + name);

//        if(identifiers.get(name) instanceof HashMap<?,?>)
//        {
//            StringBuilder sb =  new StringBuilder();
//            sb.append("{");
//
//            Map<Object, Object> table = (Map<Object, Object>) identifiers.get(name);
//            for (Object key : table.keySet())
//                sb.append(key).append("=").append(table.get(key)).append(", ");
//
//            return sb.substring(0, sb.length()-2) + "}";
//        }
        else if(identifiers.get(name) == null)
            return null;
        else
            return identifiers.get(name);
    }

    @SuppressWarnings("unchecked")

    public static Object getIdentifier(String name, List<Object> indexes)
    {
        if (!identifiers.containsKey(name))
            new ParsingError("Not an identifier: " + name);

        if(! getIdentifierType(name).equals("TABLE"))
            new ParsingError("Not a table: " + name);

        Object value = identifiers.get(name);

        for (Object index : indexes)
        {
            if(getType(value) != "TABLE")
                new RuntimeError(0, "Not a table: " + value);

            if(!( (HashMap<Object, Object>) value).containsKey(index))
                new RuntimeError(0, "Not such key as " + index + " in " + name);

            value = ((HashMap<Object, Object>) value).get(index);
        }

        return value;

    }

    public static String getIdentifierType(String name)
    {
        if (!identifiers.containsKey(name))
            return "NULL";

        if(identifiers.get(name) instanceof HashMap<?,?>)
            return "TABLE";
        else if(identifiers.get(name) instanceof Integer || identifiers.get(name) instanceof Float)
            return "NUMBER";
        else if(identifiers.get(name) instanceof String)
            return "STRING";
        else if(identifiers.get(name) == null)
            return "NULL";
        else
            throw new RuntimeException("Of unexpected type " + identifiers.get(name) + " : " + name);
    }

    public static Object getType(Object value)
    {
        if(value instanceof HashMap<?,?>)
            return "TABLE";
        else if(value instanceof Integer || value instanceof Double)
            return "NUMBER";
        else if(value instanceof String)
            return "STRING";
        else if(value == null)
            return "NULL";
        else
            throw new RuntimeException("Of unexpected type " + value);
    }

    @SuppressWarnings("unchecked")
    public static Object retrieveFromTable(Object identifier, List<Object> arguments)
    {
        if(! getIdentifierType(identifier.toString()).equals("TABLE"))
            new RuntimeError(0, "Not a table");

        HashMap<Object, Object> table = (HashMap<Object, Object>) getIdentifier(identifier.toString());

        Object value = null;

        for (Object argument : arguments)
        {
            if (table.get(argument) instanceof HashMap<?, ?>)
                table = (HashMap<Object, Object>) table.get(argument);
            else
                value = table.get(argument);
        }
        return value;
    }
}
