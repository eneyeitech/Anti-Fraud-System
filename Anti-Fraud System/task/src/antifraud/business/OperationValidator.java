package antifraud.business;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class OperationValidator implements ConstraintValidator<OperationCorrect, String> {
    List<String> valueList = null;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return valueList.contains(value);
    }

    @Override
    public void initialize(OperationCorrect constraintAnnotation) {
        valueList = new ArrayList<>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClazz();

        @SuppressWarnings("rawtypes")
        Enum[] enumValArr = enumClass.getEnumConstants();

        for (@SuppressWarnings("rawtypes") Enum enumVal : enumValArr) {
            valueList.add(enumVal.toString());
        }
    }
}
