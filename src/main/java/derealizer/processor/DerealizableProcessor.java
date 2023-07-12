package derealizer.processor;

import derealizer.annotation.Derealizable;
import derealizer.processor.error.ProcessorException;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

import static javax.lang.model.element.ElementKind.CLASS;
import static javax.lang.model.element.ElementKind.INTERFACE;
import static javax.tools.Diagnostic.Kind.ERROR;

//@AutoService(Processor.class)
public class DerealizableProcessor extends AbstractSingleProcessor {

    @Override
    public void doProcess(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv)  throws ProcessorException {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Derealizable.class)) {
            if (annotatedElement.getKind() != CLASS && annotatedElement.getKind() != INTERFACE) {
                throw new ProcessorException("Only classes can be annotated with @%s", Derealizable.class.getSimpleName());
            }
            TypeElement typeElement = (TypeElement) annotatedElement;

        }
    }

    @Override
    protected Class<?> supportedAnnotation() {
        return Derealizable.class;
    }

}
