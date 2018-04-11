package com.example.demo.repository;

public class TutorialRepositoryException extends RuntimeException{
    public TutorialRepositoryException() {
    }

    public TutorialRepositoryException(String message) {
        super(message);
    }

    public TutorialRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public TutorialRepositoryException(Throwable cause) {
        super(cause);
    }

    public TutorialRepositoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
