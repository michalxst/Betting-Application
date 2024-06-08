package betsapp.air.betsapp.security;

import lombok.AllArgsConstructor;
import org.passay.*;
import org.springframework.stereotype.Component;

@Component
public class CustomPasswordValidator {
    public PasswordValidator createPasswordValidator() {
        return new PasswordValidator(
                new LengthRule(8, Integer.MAX_VALUE),
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new WhitespaceRule()
        );
    }
    public ValidationResult validatePassword(String password) {
        RuleResult result = createPasswordValidator().validate(new PasswordData(password));
        return new ValidationResult(result);
    }
    @AllArgsConstructor
    public static class ValidationResult {
        private final RuleResult ruleResult;
        public boolean isValid() {
            return ruleResult.isValid();
        }

    }
}
