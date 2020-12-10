package fr.polytech.wish.errors;

public class WishIsNotValidException extends Exception {

    public WishErrorStatus status;

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public WishIsNotValidException(WishErrorStatus status, String message) {
        super(String.format("Error %s : %s", status, message));
    }
    
}
