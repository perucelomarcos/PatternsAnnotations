package br.me.patterns.annotation.processor;

import com.squareup.javapoet.JavaFile;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import br.me.patterns.annotation.Builder;
import br.me.patterns.annotation.Singleton;
import br.me.patterns.annotation.processor.generator.BuilderGenerator;
import br.me.patterns.annotation.processor.generator.SingletonGenerator;

public class PatternsProcessor extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        if (annotations.size() > 0) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "annotations.size() : " + annotations.size());
            processAnnotations(roundEnvironment);
            return true;
        } else {
            return false;
        }
    }

    private void processAnnotations(RoundEnvironment roundEnvironment) {
        for (Element element : roundEnvironment.getElementsAnnotatedWith(Singleton.class)) {
            JavaFile javaFile = SingletonGenerator.generate(element);
            writeFile(javaFile);
        }

        for (Element element : roundEnvironment.getElementsAnnotatedWith(Builder.class)) {
            JavaFile[] files = BuilderGenerator.generate(element);

            for (JavaFile file: files) {
                writeFile(file);
            }
        }
    }

    private void writeFile(JavaFile javaFile) {
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "Erro ao gerar arquivo: " + e);
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new HashSet<>();
        Collections.addAll(types,
                Singleton.class.getCanonicalName(),
                Builder.class.getCanonicalName()
        );

        return Collections.unmodifiableSet(types);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
