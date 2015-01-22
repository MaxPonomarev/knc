

import java.util.Arrays;

class PasswordChecker {
  static final int THRESHOLD = 11;

  static int getStrength(char[] pass) {
    int numberOfCharacters = pass.length;
    int uppercaseLetters = 0;
    int lowercaseLetters = 0;
    int numbers = 0;
    int symbols = 0;
    int requirements = 0;
    int lettersOnly = 0;
    int numbersOnly = 0;
    int consecutiveUppercaseLetters = 0;
    int consecutiveLowercaseLetters = 0;
    int consecutiveNumbers = 0;
    int sequentialSymbols = 0;
    for (int i = 0; i < pass.length; i++) {
      char ch = pass[i];
      if (Character.isUpperCase(ch)) uppercaseLetters++;
      if (Character.isLowerCase(ch)) lowercaseLetters++;
      if (Character.isDigit(ch)) numbers++;
      if (!Character.isLetterOrDigit(ch)) symbols++;
      if (i > 0) {
        if (Character.isUpperCase(ch) && Character.isUpperCase(pass[i - 1])) consecutiveUppercaseLetters++;
        else if (Character.isLowerCase(ch) && Character.isLowerCase(pass[i - 1])) consecutiveLowercaseLetters++;
        else if (Character.isDigit(ch) && Character.isDigit(pass[i - 1])) consecutiveNumbers++;
      }
      if (i > 1 && ((ch == pass[i - 1] + 1 && ch == pass[i - 2] + 2) || (ch == pass[i - 1] - 1 && ch == pass[i - 2] - 2))) {
        sequentialSymbols++;
      }
    }

    Arrays.fill(pass, (char) 0);

    if (numberOfCharacters >= 5) requirements++;
    if (uppercaseLetters > 0) requirements++;
    if (lowercaseLetters > 0) requirements++;
    if (numbers > 0) requirements++;
    if (symbols > 0) requirements++;

    if (uppercaseLetters + lowercaseLetters > 0 && numbers == 0 && symbols == 0)
      lettersOnly = uppercaseLetters + lowercaseLetters;
    if (numbers > 0 && uppercaseLetters == 0 && lowercaseLetters == 0 && symbols == 0) numbersOnly = numbers;

    int result = numberOfCharacters * 4;
    if (uppercaseLetters > 0) result += (numberOfCharacters - uppercaseLetters) * 2;
    if (lowercaseLetters > 0) result += (numberOfCharacters - lowercaseLetters) * 2;
    if (numbersOnly == 0) result += numbers * 4;
    result += symbols * 6;
    if (requirements >= 4) result += requirements * 2;
    result -= lettersOnly;
    result -= numbersOnly;
    result -= consecutiveUppercaseLetters * 2;
    result -= consecutiveLowercaseLetters * 2;
    result -= consecutiveNumbers * 2;
    result -= sequentialSymbols * 3;
    if (result < 0) result = 0;
    if (result > 100) result = 100;
    return result;
  }
}
