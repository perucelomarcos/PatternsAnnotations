package br.me.patterns.annotation.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

import br.me.patterns.annotation.processor.Utils;

public class SingletonGenerator {

    static final String PATTERN_NAME = "Singleton";
    static final String CREATED_CLASS_SUFFIX = "_";
    static final String GET_INSTANCE_METHOD_NAME = "getInstance";
    static final String INSTANCE_FIELD_NAME = "instance";

    public static JavaFile generate(Element element) {
        String targetFileName = getTargetFileName(element);
        ClassName className = getTargetClassName(element, targetFileName);

        TypeName type = TypeName.get(element.asType());
        TypeSpec singletonTypeSpec = getClassTypeSpec(type, className);

        return getJavaFile(element, singletonTypeSpec);
    }

    static String getTargetFileName(Element element) {
        return element.getSimpleName().toString() + SingletonGenerator.CREATED_CLASS_SUFFIX;
    }

    private static ClassName getTargetClassName(Element element, String targetFileName) {
        return ClassName.get(Utils.getPackage(element).toString(), targetFileName);
    }

    static TypeSpec getClassTypeSpec(TypeName type, ClassName className) {
        FieldSpec instanceField = getInstanceFieldSpec(type);
        MethodSpec constructor = getConstructorSpec();
        MethodSpec getInstanceMethod = getInstanceMethodSpec(type);

        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .superclass(type)
                .addField(instanceField)
                .addMethod(constructor)
                .addMethod(getInstanceMethod)
                .build();
    }

    static FieldSpec getInstanceFieldSpec(TypeName type) {
        return FieldSpec.builder(type, SingletonGenerator.INSTANCE_FIELD_NAME, Modifier.PRIVATE, Modifier.STATIC).build();
    }

    static MethodSpec getConstructorSpec() {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PRIVATE).build();
    }

    static MethodSpec getInstanceMethodSpec(TypeName type) {
        return MethodSpec.methodBuilder(GET_INSTANCE_METHOD_NAME)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(type)
                .beginControlFlow("if ($L == null)", INSTANCE_FIELD_NAME)
                .addStatement("$L = new $T()", INSTANCE_FIELD_NAME, type)
                .endControlFlow()
                .addStatement("return $L", INSTANCE_FIELD_NAME)
                .build();
    }

    private static JavaFile getJavaFile(Element element, TypeSpec singletonTypeSpec) {
        return JavaFile.builder(Utils.getPackage(element).getQualifiedName().toString(), singletonTypeSpec).build();
    }
}
