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
    private int start = 0; // points to the first character in the lexeme being scanned
    private int current = 0; // points at the character currently being considered
    private int line = 1; // tracks what source line "current" is on

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
}
