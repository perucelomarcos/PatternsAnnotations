package br.me.patterns.annotation.processor.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.Serializable;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import br.me.patterns.annotation.processor.Utils;

public class BuilderGenerator {

    static final String PATTERN_NAME = "Builder";
    static final String CREATED_CLASS_SUFFIX = "_";

    public static JavaFile[] generate(Element element) {
        String targetFileName = getTargetFileName(element);
        ClassName className = getTargetClassName(element, targetFileName);

        BuilderClassHelper builderClassHelper = new BuilderClassHelper(element, className);
        BuiltClassHelper builtClassHelper = new BuiltClassHelper(className);

        for (Element enclosedElement: element.getEnclosedElements()) {
            if (! isElementToProcess(enclosedElement)) {
                continue;
            }

            addBuilderMethodToBuilderClass(builderClassHelper, enclosedElement, className);
            addFieldToBuiltClass(builtClassHelper, enclosedElement);
        }

        addBuildMethodToBuilderClass(builderClassHelper, element, className);
        addToStringMethodToBuiltClass(builtClassHelper);

        JavaFile builderJavaFile = getJavaFile(element, builderClassHelper.builderTypeSpec.build());
        JavaFile builtJavaFile = getJavaFile(element, builtClassHelper.builtTypeSpec.build());

        return new JavaFile[] {builderJavaFile, builtJavaFile};
    }

    static String getTargetFileName(Element element) {
        return element.getSimpleName().toString() + SingletonGenerator.CREATED_CLASS_SUFFIX;
    }

    private static ClassName getTargetClassName(Element element, String targetFileName) {
        return ClassName.get(Utils.getPackage(element).toString(), targetFileName);
    }

    private static boolean isElementToProcess(Element e) {
        TypeMirror typeMirror = e.asType();
        return typeMirror.getKind().isPrimitive() || typeMirror.getKind().equals(TypeKind.DECLARED);
    }

    private static void addBuilderMethodToBuilderClass(BuilderClassHelper builderClassHelper, Element e, ClassName returnType) {
        String fieldName = e.getSimpleName().toString();
        TypeMirror builderParameter = e.asType();
        String builderName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

        MethodSpec builderMethodSpec = MethodSpec.methodBuilder(builderName)
                .addModifiers(Modifier.PUBLIC)
                .returns(returnType)
                .addParameter(TypeName.get(builderParameter), fieldName)
                .addStatement("super.$L=$L", fieldName, fieldName)
                .addStatement("return this")
                .build();

        builderClassHelper.visit(builderMethodSpec);
        builderClassHelper.visit(fieldName);
    }

    private static void addFieldToBuiltClass(BuiltClassHelper builtClassHelper, Element e) {
        String fieldName = e.getSimpleName().toString();
        TypeMirror fieldParameter = e.asType();

        FieldSpec fieldSpec = FieldSpec.builder(TypeName.get(fieldParameter), fieldName)
                .addModifiers(Modifier.PUBLIC)
                .build();

        builtClassHelper.visit(fieldSpec);
        builtClassHelper.visit(fieldName);
    }

    private static void addBuildMethodToBuilderClass(BuilderClassHelper builderClassHelper, Element element, ClassName className) {
        String builtFileName = getClassNameToBuild(className);
        ClassName justCreatedClassName = ClassName.get(Utils.getPackage(element).getQualifiedName().toString(), builtFileName);

        String builtClassVarName = getClassNameToBuild(className).toLowerCase();
        MethodSpec.Builder buildMethod = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(justCreatedClassName)
                .addStatement("$T $L = new $T()", justCreatedClassName, builtClassVarName, justCreatedClassName)
                .addCode(builderClassHelper.getBuildMethodCodeBlock())
                .addStatement("return $L", builtClassVarName);

        builderClassHelper.visit(buildMethod.build());
    }

    private static void addToStringMethodToBuiltClass(BuiltClassHelper builtClassHelper) {
        MethodSpec.Builder toStringMethodBuilder = MethodSpec.methodBuilder("toString")
                .returns(String.class)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addStatement(builtClassHelper.getToStringCodeBlock());

        builtClassHelper.visit(toStringMethodBuilder.build());
    }

    static class BuilderClassHelper {
        ClassName className;
        TypeSpec.Builder builderTypeSpec;
        CodeBlock.Builder buildMethodBuilder;

        BuilderClassHelper(Element element, ClassName className) {
            TypeName type = TypeName.get(element.asType());
            this.className = className;
            builderTypeSpec = getBuilderClassTypeSpec(type, className);
            buildMethodBuilder = CodeBlock.builder();
        }

        void visit(MethodSpec methodSpec) {
            builderTypeSpec.addMethod(methodSpec);
        }

        void visit(String fieldName) {
            String builtClassVarName = getClassNameToBuild(className).toLowerCase();
            buildMethodBuilder.add("$L.$L = $L;\n", builtClassVarName, fieldName, fieldName);
        }

        CodeBlock getBuildMethodCodeBlock() {
            return buildMethodBuilder.build();
        }
    }

    static class BuiltClassHelper {
        TypeSpec.Builder builtTypeSpec;
        CodeBlock.Builder toStringBuilder;

        BuiltClassHelper(ClassName className) {
            builtTypeSpec = getBuiltClassTypeSpec(className);
            String builtFileName = getClassNameToBuild(className);
            toStringBuilder = CodeBlock.builder()
                    .add("return \"\" + \n")
                    .indent()
                    .add("\"$L {\" +", builtFileName);
        }

        void visit(FieldSpec fieldSpec) {
            builtTypeSpec.addField(fieldSpec);
        }

        void visit(MethodSpec methodSpec) {
            builtTypeSpec.addMethod(methodSpec);
        }

        void visit(String fieldName) {
            toStringBuilder.add("\n\"$L=\" + $L + \",\" +", fieldName, fieldName);
        }

        CodeBlock getToStringCodeBlock() {
            toStringBuilder.add("\n\"}\"");
            return toStringBuilder.build();
        }
    }

    static String getClassNameToBuild(ClassName className) {
        String fileName = className.simpleName();
        int idx = fileName.indexOf(PATTERN_NAME);
        fileName = fileName.substring(0, idx);

        return fileName;
    }

    static TypeSpec.Builder getBuilderClassTypeSpec(TypeName type, ClassName className) {
        MethodSpec constructor = getConstructorSpec();

        return TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC)
                .superclass(type)
                .addMethod(constructor);
    }

    static TypeSpec.Builder getBuiltClassTypeSpec(ClassName className) {
        MethodSpec constructor = getConstructorSpec();
        String builtFileName = getClassNameToBuild(className);

        return TypeSpec.classBuilder(builtFileName)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(Serializable.class)
                .addMethod(constructor);
    }

    static MethodSpec getConstructorSpec() {
        return MethodSpec.constructorBuilder().addModifiers(Modifier.PUBLIC).build();
    }

    private static JavaFile getJavaFile(Element element, TypeSpec typeSpec) {
        return JavaFile.builder(Utils.getPackage(element).getQualifiedName().toString(), typeSpec).build();
    }
}
