package derealizer.processor;

import derealizer.annotation.Derealizable;

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
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Derealizable.class)) {
            if (annotatedElement.getKind() != CLASS && annotatedElement.getKind() != INTERFACE) {
                error(annotatedElement, "Only types can be annotated with @%s", Derealizable.class.getSimpleName());
                return true;
            }

        }
        return false;
    }

    @Override
    protected Class<?> supportedAnnotation() {
        return Derealizable.class;
    }

    private void error(Element e, String msg, Object... args) {
        messager.printMessage(ERROR, String.format(msg, args), e);
    }

}
