package derealizer.processor.error;

import derealizer.processor.AbstractSingleProcessor;
import derealizer.processor.DerealizableProcessor;

import javax.annotation.processing.RoundEnvironment;
import java.util.Set;

import static java.lang.String.format;

/**
 * {@link RuntimeException} thrown by the {@link DerealizableProcessor} when an error occurs during processing. Do not
 * use this exception anywhere else other than in subclasses of {@link AbstractSingleProcessor}.
 * <br><br>
 * Internally, this is handled within the {@link AbstractSingleProcessor#process(Set, RoundEnvironment) process} method.
 *
 * @author Lunkle
 */
public class ProcessorException extends RuntimeException {

    public ProcessorException(String message, Object... args) {
        super(format(message, args));
    }

}
