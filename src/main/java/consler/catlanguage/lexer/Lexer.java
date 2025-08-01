package consler.catlanguage.lexer;


import consler.catlanguage.lexer.token.Token;
import consler.catlanguage.lexer.token.TokenType;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer
{
    private static final String[] KEYWORDS = new String[]{
            "createText",
            "log",
            "if",
            "else",
            "while"
    };
    private static final String[] EVENTS = new String[]{
            "onStart",
            "onClick"
    };
    private static final String IDENTIFIER = "[a-zA-Z_][a-zA-Z0-9_]*";
    private static final String INTEGER = "\\d+";
    private static final String STRING = "\"[^\"]*\"";
    private static final String SYMBOL = "[+\\-*/=():!><]";
    private static final String INDENTATION = "\\t|( {4})";

    public static List<Token> tokenize(String input)
    {
        List<Token> tokens = new ArrayList<>();
        String[] lines = input.split("\n");

        for (int line_count = 0; line_count < lines.length; line_count++)
        {
            String line = lines[line_count];

            if (line.isEmpty()) continue;

            Pattern pattern = Pattern.compile(IDENTIFIER + "|" + INTEGER + "|" + STRING + "|" + SYMBOL + "|" + INDENTATION);
            Matcher matcher = pattern.matcher(line);

            while (matcher.find())
            {
                String token_value = matcher.group();

                if (isKeyword(token_value))
                {
                    tokens.add(new Token(TokenType.KEYWORD, token_value, line_count));

                }
                else if (isEvent(token_value))
                {
                    tokens.add(new Token(TokenType.EVENT, token_value, line_count));

                }
                else if (token_value.matches(IDENTIFIER))
                {
                    tokens.add(new Token(TokenType.IDENTIFIER, token_value, line_count));

                }
                else if (token_value.matches(INTEGER))
                {
                    tokens.add(new Token(TokenType.INTEGER, token_value, line_count));

                }
                else if (token_value.matches(STRING))
                {
                    tokens.add(new Token(TokenType.STRING, token_value.substring(1, token_value.length() - 1), line_count));

                }
                else if (token_value.matches(SYMBOL))
                {
                    tokens.add(new Token(TokenType.SYMBOL, token_value, line_count));

                }
                else if (token_value.matches(INDENTATION))
                {
                    tokens.add(new Token(TokenType.INDETATION, token_value, line_count));

                }

            }

        }

        return tokens;

    }

    private static boolean isKeyword(String tokenValue)
    {
        for (String keyword : KEYWORDS)
        {
            if (keyword.equals(tokenValue))
            {
                return true;

            }

        }

        return false;

    }

    private static boolean isEvent(String tokenValue)
    {
        for (String event : EVENTS)
        {
            if (event.equals(tokenValue))
            {
                return true;

         }

        }

        return false;

    }


}
