package fr.polytech.webservices.models.mapper.errors;

public class BadFieldException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BadFieldException(String error) {
        super(error);
    }
    
}
