package ru.lena.restaurant.utils.exсeption;

public class VotingForbiddenException extends ApplicationException {
    public static final String VOTING_FORBIDDEN = "exception.user.votingForbidden";
    public VotingForbiddenException() {
        super(ErrorType.VALIDATION_ERROR, VOTING_FORBIDDEN);
    }
}
