package fr.polytech.webservices.models.mapper.errors;

public class BadAccessGetterSetterMethod extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public BadAccessGetterSetterMethod(String error) {
        super(error);
    }
    
}