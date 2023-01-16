package org.seek.aips.exceptions;

public class InvalidFileFormatException extends RuntimeException{

    public InvalidFileFormatException(Throwable cause) {
        super("This does not follow the expected file format.", cause);
    }

}
