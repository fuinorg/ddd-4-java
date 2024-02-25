/**
 * Copyright (C) 2013 Future Invent Informationsmanagement GmbH. All rights
 * reserved. <http://www.fuin.org/>
 * <p>
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 * <p>
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.fuin.ddd4j.core;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public final class HasEntityTypeConstantValidatorTest {

    private static final String FIELD_NAME = "TYPE";

    private static Validator validator;

    @BeforeAll
    static void beforeAll() {
        try (final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            validator = validatorFactory.getValidator();
        }
    }

    @Test
    public void testValid() {
        assertThat(validator.validate(new MyClassValid())).isEmpty();
        assertThat(HasEntityTypeConstantValidator.extractValue(MyClassValid.class, FIELD_NAME)).isEqualTo(new StringBasedEntityType("XYZ"));
    }

    @Test
    public void testNotStatic() {

        assertThat(first(validator.validate(new MyClassNotStatic()))).contains("#1");

        assertThatThrownBy(
                () -> HasEntityTypeConstantValidator.extractValue(MyClassNotStatic.class, FIELD_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("#1");

    }

    @Test
    public void testNotPublic() {

        assertThat(first(validator.validate(new MyClassNotPublic()))).contains("#2");

        assertThatThrownBy(
                () -> HasEntityTypeConstantValidator.extractValue(MyClassNotPublic.class, FIELD_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("#2");

    }

    @Test
    public void testWrongReturnType() {

        assertThat(first(validator.validate(new MyClassWrongType()))).contains("#3");

        assertThatThrownBy(
                () -> HasEntityTypeConstantValidator.extractValue(MyClassWrongType.class, FIELD_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("#3");

    }

    @Test
    public void testWrongReturn() {

        assertThat(first(validator.validate(new MyClassNullValue()))).contains("#4");

        assertThatThrownBy(
                () -> HasEntityTypeConstantValidator.extractValue(MyClassNullValue.class, FIELD_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("#4");

    }

    @Test
    public void testNoMethod() {

        assertThat(first(validator.validate(new MyClassNoField()))).contains("#2");

        assertThatThrownBy(
                () -> HasEntityTypeConstantValidator.extractValue(MyClassNoField.class, FIELD_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("#2");

    }

    @Test
    public void testNotFinal() {

        assertThat(first(validator.validate(new MyClassNotFinal()))).contains("#5");

        assertThatThrownBy(
                () -> HasEntityTypeConstantValidator.extractValue(MyClassNotFinal.class, FIELD_NAME))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("#5");

    }

    private static String first(Set<?> violations) {
        return violations.stream().map(v -> ((ConstraintViolation<?>) v).getMessage()).findFirst().orElse(null);
    }

    @HasEntityTypeConstant
    public static final class MyClassValid {
        public static final EntityType TYPE = new StringBasedEntityType("XYZ");
    }

    @HasEntityTypeConstant
    public static final class MyClassNotStatic {
        public final EntityType TYPE = new StringBasedEntityType("XYZ");
    }

    @HasEntityTypeConstant
    public static final class MyClassNotPublic {
        protected static final EntityType TYPE = new StringBasedEntityType("XYZ");
    }

    @HasEntityTypeConstant
    public static final class MyClassNoField {
    }

    @HasEntityTypeConstant
    public static final class MyClassWrongType {
        public static final Integer TYPE = 123;
    }

    @HasEntityTypeConstant
    public static final class MyClassNullValue {
        public static final EntityType TYPE = null;
    }

    @HasEntityTypeConstant
    public static final class MyClassNotFinal {
        public static EntityType TYPE = new StringBasedEntityType("XYZ");
    }


}
