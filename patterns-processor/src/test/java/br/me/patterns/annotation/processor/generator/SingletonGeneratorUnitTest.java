package br.me.patterns.annotation.processor.generator;

import com.google.testing.compile.CompilationRule;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

import static com.google.common.truth.Truth.assertThat;

@RunWith(JUnit4.class)
public class SingletonGeneratorUnitTest {

    @Rule
    public CompilationRule compilationRule = new CompilationRule();

    private TypeElement getElementToApplyPattern() {
        return compilationRule.getElements().getTypeElement(MathSingletonToTest.class.getCanonicalName());
    }

    @Test
    public void generatedFileContent_matchesTemplate() {
        TypeElement elementToApplyPattern = getElementToApplyPattern();
        JavaFile generatedFile = SingletonGenerator.generate(elementToApplyPattern);

        assertThat(generatedFile.toString()).isEqualTo(expectedFileContent());
    }

    @Test
    public void generatedFilename_matchesFilePattern() {
        TypeElement elementToApplyPattern = getElementToApplyPattern();
        String targetFileName = SingletonGenerator.getTargetFileName(elementToApplyPattern);

        assertThat(targetFileName).isEqualTo(elementToApplyPattern.getSimpleName().toString() + SingletonGenerator.CREATED_CLASS_SUFFIX);
        assertThat(targetFileName).contains(SingletonGenerator.PATTERN_NAME);
    }

    @Test
    public void instanceField_isPrivateFinalAndStatic() {
        TypeName mockType = ParameterizedTypeName.get(MathSingletonToTest.class);
        FieldSpec instanceField = SingletonGenerator.getInstanceFieldSpec(mockType);

        assertThat(instanceField.modifiers).containsExactly(Modifier.PRIVATE, Modifier.FINAL, Modifier.STATIC);
        assertThat(instanceField.name).isEqualTo(SingletonGenerator.INSTANCE_FIELD_NAME);
        assertThat(instanceField.type.getClass().getSimpleName()).isEqualTo(mockType.getClass().getSimpleName());
    }

    @Test
    public void constructor_isPrivate() {
        MethodSpec constructor = SingletonGenerator.getConstructorSpec();

        assertThat(constructor.modifiers).contains(Modifier.PRIVATE);
    }

    @Test
    public void getInstanceMethod_isPublicAndStatic() {
        TypeName mockType = ParameterizedTypeName.get(MathSingletonToTest.class);
        MethodSpec method = SingletonGenerator.getInstanceMethodSpec(mockType);

        assertThat(method.modifiers).containsExactly(Modifier.PUBLIC, Modifier.STATIC);
        assertThat(method.name).isEqualTo(SingletonGenerator.GET_INSTANCE_METHOD_NAME);
        assertThat(method.returnType).isEqualTo(mockType);
    }

    @Test
    public void singletonClass_isPublicAndExtendsOriginalClass() {
        TypeName mockType = ParameterizedTypeName.get(MathSingletonToTest.class);
        String originalFile = MathSingletonToTest.class.getCanonicalName();
        String fileToCreate = MathSingletonToTest.class.getSimpleName() + SingletonGenerator.CREATED_CLASS_SUFFIX;
        ClassName className = ClassName.get(MathSingletonToTest.class.getPackage().getName(), fileToCreate);

        TypeSpec classType = SingletonGenerator.getClassTypeSpec(mockType, className);

        assertThat(classType.modifiers).contains(Modifier.PUBLIC);
        assertThat(classType.superclass.toString()).isEqualTo(originalFile);
        assertThat(classType.name).isEqualTo(fileToCreate);
    }

    private String expectedFileContent() {
        return "" +
                "package br.me.patterns.annotation.processor.generator;\n" +
                "\n" +
                "public class MathSingletonToTest_ extends SingletonGeneratorUnitTest.MathSingletonToTest {\n" +
                "  private static final SingletonGeneratorUnitTest.MathSingletonToTest INSTANCE = new SingletonGeneratorUnitTest.MathSingletonToTest();\n" +
                "\n" +
                "  private MathSingletonToTest_() {\n" +
                "  }\n" +
                "\n" +
                "  public static SingletonGeneratorUnitTest.MathSingletonToTest getInstance() {\n" +
                "    return INSTANCE;\n" +
                "  }\n" +
                "}\n";
    }

    class MathSingletonToTest {
        public int sum(int a, int b) {
            return a + b;
        }
        public int subtract(int a, int b) {
            return a - b;
        }
        public float multiply(float a, float b) {
            return a * b;
        }
        public float divide(float a, float b) {
            return a / b;
        }
        MathSingletonToTest() {}
    }
}