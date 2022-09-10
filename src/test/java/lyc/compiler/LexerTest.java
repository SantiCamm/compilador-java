package lyc.compiler;

import lyc.compiler.factories.LexerFactory;
import lyc.compiler.model.*;
import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.google.common.truth.Truth.assertThat;
import static lyc.compiler.constants.Constants.MAX_LENGTH;
import static org.junit.jupiter.api.Assertions.assertThrows;



public class LexerTest {

  private Lexer lexer;


  @Test
  public void comment() throws Exception{
    scan("/*This is a comment*/");
    assertThat(nextToken()).isEqualTo(ParserSym.EOF);
  }

  @Test
  public void invalidStringConstantLength() {
    assertThrows(InvalidLengthException.class, () -> {
      scan("\"%s\"".formatted(getRandomString(2)));
      nextToken();
    });
  }


  @Test
  public void validString() throws Exception {
    scan("\"%s\"".formatted(getRandomString(1)));
    assertThat(nextToken()).isEqualTo(ParserSym.STRING_CONSTANT);
  }

<<<<<<< Updated upstream
  @Disabled
  @Test
  public void invalidIdLength() {
    assertThrows(InvalidLengthException.class, () -> {
      scan(getRandomString(2));
=======
  @Test
  public void invalidIdLength() {
    assertThrows(InvalidLengthException.class, () -> {
      scan("\"" + getRandomString(2) + "\"");
>>>>>>> Stashed changes
      nextToken();
    });
  }

//  @Disabled
  @Test
  public void invalidPositiveIntegerConstantValue() {
    assertThrows(InvalidIntegerException.class, () -> {
      scan("%d".formatted(9223372036854775807L));
      nextToken();
    });
  }

  @Disabled
  @Test
  public void invalidNegativeIntegerConstantValue() {
    assertThrows(InvalidIntegerException.class, () -> {
      scan("%d".formatted(-9223372036854775807L));
      nextToken();
    });
  }

<<<<<<< Updated upstream
=======
  @Test
  public void invalidExponentFloatConstantValue() {
    assertThrows(InvalidFloatException.class, () -> {
      scan(String.valueOf(300.120005));
      nextToken();
    });
  }

  @Test
  public void invalidMantissaFloatConstantValue() {
    assertThrows(InvalidFloatException.class, () -> {
      scan(String.valueOf(12.22222222222222));
      nextToken();
    });
  }

>>>>>>> Stashed changes
//  @Disabled
  @Test
  public void assignmentWithExpressions() throws Exception {
    scan("c=d*(e-21)/4");
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.ASSIG);
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.MULT);
    assertThat(nextToken()).isEqualTo(ParserSym.OPEN_BRACKET);
    assertThat(nextToken()).isEqualTo(ParserSym.IDENTIFIER);
    assertThat(nextToken()).isEqualTo(ParserSym.SUB);
    assertThat(nextToken()).isEqualTo(ParserSym.INTEGER_CONSTANT);
    assertThat(nextToken()).isEqualTo(ParserSym.CLOSE_BRACKET);
    assertThat(nextToken()).isEqualTo(ParserSym.DIV);
    assertThat(nextToken()).isEqualTo(ParserSym.INTEGER_CONSTANT);
    assertThat(nextToken()).isEqualTo(ParserSym.EOF);
  }

//  @Disabled
  @Test
  public void unknownCharacter() {
    assertThrows(UnknownCharacterException.class, () -> {
      scan("#");
      nextToken();
    });
  }

  @AfterEach
  public void resetLexer() {
    lexer = null;
  }

  private void scan(String input) {
    lexer = LexerFactory.create(input);
  }

  private int nextToken() throws IOException, CompilerException {
    return lexer.next_token().sym;
  }

  private static String getRandomString(int multiplier) {
    return new RandomStringGenerator.Builder()
            .filteredBy(CharacterPredicates.LETTERS)
            .withinRange('a', 'z')
            .build().generate((MAX_LENGTH * multiplier)-2); //-2 removes the length of ""
  }

}
