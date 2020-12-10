package fr.polytech.webservices.models.mapper.errors;

public class NotCompatibleTypeException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public NotCompatibleTypeException(String error) {
        super(error);
    }
    
}