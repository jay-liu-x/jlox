package com.craftinginterpreters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.craftinginterpreters.TokenType.*;

class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    // These properties help keep track of where the scanner is in the source code.
    private int start = 0; // points to the first character in the lexeme being scanned.
    private int current = 0; // points at the character currently being considered.
    private int line = 1; // tracks what source line "current" is on.

    Scanner(String source) {
        this.source = source;
    }

    List<Token> scanTokens() {
        while (!isAtEnd()) {
            // At the beginning of next lexeme.
            start = current;
            scanToken();
        }

        // Appending one final “end of file” token.
        // It isn’t strictly needed, but it makes our parser a little cleaner.
        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }

    // Helper function that tells us if we’ve consumed all characters.
    private boolean isAtEnd() {
        return current >= source.length();
    }

    // Recognizing lexemes of a single token.
    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
            default:
                /*
                 * Note also that we keep scanning when a syntax error is detected.
                 * There may be other errors later in the program.
                 * It gives our users a better experience if we detect as many of those as possible in one go.
                 * Otherwise, they see one tiny error and fix it, only to have the next error appear, and so on.
                 * 
                 * Don't worry. Since `hadError` gets set, we’ll never try to execute any of the code,
                 * even though we keep going and scan the rest of it.
                 */
                Lox.error(line, String.format("Unsupported character in \"%s\"", source));
                break;
        }
    }

    // Consumes the next character in the source file and returns it.
    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    // Adds single-character type token to "tokens".
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    // Adds literal type token to "tokens".
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
