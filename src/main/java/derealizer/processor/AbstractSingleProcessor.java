package derealizer.processor;

import javax.annotation.processing.AbstractProcessor;
import derealizer.Derealizable;
import derealizer.processor.error.ProcessorException;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.tools.Diagnostic.Kind.ERROR;
import javax.lang.model.util.Types;
import java.util.Collections;
import java.util.Set;

public abstract class AbstractSingleProcessor extends AbstractProcessor {

    protected Types typeUtils;
    protected Elements elementUtils;
    protected Filer filer;
    protected Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment env){
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        elementUtils = processingEnv.getElementUtils();
        filer = processingEnv.getFiler();
        messager = processingEnv.getMessager();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = Collections.emptySet();
        annotations.add(this.supportedAnnotation().getCanonicalName());
        return annotations;
    }

    @Override
    public final boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try{
            doProcess(annotations, roundEnv);
            return true;
        } catch (ProcessorException e){
            messager.printMessage(ERROR, e.getMessage());
        }
        return false;
    }

    abstract void doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) throws ProcessorException;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    protected abstract Class<?> supportedAnnotation();

}
