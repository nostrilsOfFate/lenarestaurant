package ru.lena.restaurant.utils.exсeption;

public class IllegalRequestDataException extends RuntimeException {

    public IllegalRequestDataException(String msg) {
        super(msg);
    }
}
