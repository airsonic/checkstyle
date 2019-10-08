package org.airsonic.checkstyle;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.Objects;

public class LoggerNameCheck extends AbstractCheck {

    public static final String EXPECTED_VARIABLE_NAME = "LOG";
    public static final String LOGGER_CLASS_NAME = "Logger";

    public LoggerNameCheck() {

    }

    public int[] getDefaultTokens() {
        return getRequiredTokens();
    }

    public int[] getAcceptableTokens() {
        return getRequiredTokens();
    }

    public int[] getRequiredTokens() {
        return new int[] {TokenTypes.VARIABLE_DEF};
    }

    @Override
    public void visitToken(DetailAST ast) {
        if (isRelevantLogger(ast)) {
            final DetailAST nameAST = ast.findFirstToken(TokenTypes.IDENT);
            if (!Objects.equals(nameAST.getText(), EXPECTED_VARIABLE_NAME)) {
                log(nameAST.getLineNo(), "Logger variable name must be " + EXPECTED_VARIABLE_NAME);
            }
            final DetailAST modifiersAST = ast.findFirstToken(TokenTypes.MODIFIERS);
            final boolean isStatic = modifiersAST.findFirstToken(TokenTypes.LITERAL_STATIC) != null;
            if (!isStatic) {
                log(modifiersAST.getLineNo(), "Logger variable must be static");
            }
            final boolean isFinal = modifiersAST.findFirstToken(TokenTypes.FINAL) != null;
            if (!isFinal) {
                log(modifiersAST.getLineNo(), "Logger variable must be final");
            }
            final boolean isPrivate = modifiersAST.findFirstToken(TokenTypes.LITERAL_PRIVATE) != null;
            if (!isPrivate) {
                log(modifiersAST.getLineNo(), "Logger variable must be private");
            }
        }
    }

    protected final boolean isRelevantLogger(DetailAST ast) {
        final DetailAST classAST = ast.findFirstToken(TokenTypes.TYPE);
        if(classAST != null) {
            final DetailAST identAST = classAST.findFirstToken(TokenTypes.IDENT);
            if(identAST != null) {
                return Objects.equals(identAST.getText(), LOGGER_CLASS_NAME);
            }
        }
        return false;
    }
}
