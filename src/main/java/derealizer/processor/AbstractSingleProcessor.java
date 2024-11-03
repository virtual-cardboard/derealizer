package derealizer.processor;

import static javax.tools.Diagnostic.Kind.ERROR;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Collections;
import java.util.Set;

import derealizer.processor.error.ProcessorException;

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
        return Collections.singleton(this.supportedAnnotation().getCanonicalName());
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
