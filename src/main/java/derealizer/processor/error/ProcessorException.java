package derealizer.processor.error;

import static java.lang.String.format;

public class ProcessorException extends RuntimeException{

    public ProcessorException(String message,  Object... args){
        super(format(message, args));
    }

}
