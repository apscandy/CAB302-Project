package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.error.UserAlreadyLoggedInException;
import com.cab302.cab302project.error.authentication.*;
import com.cab302.cab302project.model.user.IUserDAO;
import com.cab302.cab302project.model.user.SqliteUserDAO;
import com.cab302.cab302project.model.user.User;
import com.cab302.cab302project.model.userSecQuestions.IUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.SqliteUserSecurityQuestionDAO;
import com.cab302.cab302project.model.userSecQuestions.UserSecurityQuestion;
import com.cab302.cab302project.util.PasswordUtils;
import com.cab302.cab302project.util.RegexValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * The {@code AuthenticationService} class provides methods to handle
 * user authentication operations like registration, password reset and security question checks.
 * This class relies on {@link IUserDAO} and {@link IUserSecurityQuestionDAO} for database operations.
 *
 * <p> Any violation of these rules will throw relevant custom runtime exceptions.</p>
 * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
 */
public final class AuthenticationService {

    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);

    private static final String MSG_PASSWORD_EMPTY = "password is empty";
    private static final String MSG_EMAIL_EMPTY = "email is empty";
    private static final String MSG_INVALID_EMAIL_FORMAT = "invalid email format";
    private static final String MSG_USER_NOT_FOUND = "user not found";
    private static final String MSG_INCORRECT_PASSWORD = "incorrect password provided";
    private static final String MSG_PASSWORD_REQUIREMENT = "password did not meet the requirement";
    private static final String MSG_EMAIL_ALREADY_IN_USE = "Email already in use";
    private static final String MSG_ANSWER_ONE_EMPTY = "answerOne is empty";
    private static final String MSG_ANSWER_TWO_EMPTY = "answerTwo is empty";
    private static final String MSG_ANSWER_THREE_EMPTY = "answerThree is empty";
    private static final String MSG_ANSWER_ONE_MISMATCH = "Answer one does not match";
    private static final String MSG_ANSWER_TWO_MISMATCH = "Answer two does not match";
    private static final String MSG_ANSWER_THREE_MISMATCH = "Answer three does not match";

    private final IUserDAO userDAO;
    private final IUserSecurityQuestionDAO userSecurityQuestionDAO;

    /**
     * Constructor method for {@code AuthenticationService} class
     * <p>This creates default DAOs for user and security questions.</p>
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     */
    public AuthenticationService() {
        this.userDAO = new SqliteUserDAO();
        this.userSecurityQuestionDAO = new SqliteUserSecurityQuestionDAO();
    }

    /**
     * This method authenticates a user by verifying the provided email and input plain-text password.
     * <p> User's authentication status is first checked against the application state to see if user is already authenticated
     * ; if so, it throws a {@link UserAlreadyLoggedInException}. It then checks required params are non-empty,
     * and that the email passes the regex validation. If the user exists in the database and the password matches the stored hashed password,
     * the user is logged in via {@link ApplicationState#login(User)}. </p>
     *
     * @param email    user's email address
     * @param password user's plain-text password
     * @return {@code true} if authentication succeeds
     * @throws UserAlreadyLoggedInException if a user is already logged in
     * @throws PasswordEmptyException if the password is empty
     * @throws EmailEmptyException if the email is empty
     * @throws InvalidEmailFormatException if the email format is invalid
     * @throws UserNotFoundException if no user with the given email is found
     * @throws PasswordComparisonException if the password does not match the stored hash
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     * @author Andrew Clarke (a40.clarke@connect.edu.au)
     */
    public boolean authenticate(String email, String password) throws RuntimeException {
        if (ApplicationState.isUserLoggedIn()){
            throw new UserAlreadyLoggedInException();
        }
        if (password == null || password.trim().isEmpty()) {
            throw new PasswordEmptyException(MSG_PASSWORD_EMPTY);
        }
        if (email == null || email.trim().isEmpty()) {
            throw new EmailEmptyException(MSG_EMAIL_EMPTY);
        }
        try {
            emailCheck(email);
        } catch (EmailAlreadyInUseException ignored){}
        User user = userDAO.getUser(email);
        if (user == null) {
            throw new UserNotFoundException(MSG_USER_NOT_FOUND);
        }
        String userPassword = user.getPassword();
        String hashedPassword = PasswordUtils.hashSHA256(password);
        if (!userPassword.equals(hashedPassword)) {
            throw new PasswordComparisonException(MSG_INCORRECT_PASSWORD);
        }
        ApplicationState.login(user);
        return true;
    }

    /**
     * This method checks if the given email is not found in the database. If the method
     * completes without exception, it returns {@code true}, meaning the email is free to use (no user found).
     * <p> If a user with that email exists, the method throws
     * {@link EmailAlreadyInUseException}. If the email is blank or invalid by
     * regex, it throws the respective exception.</p>
     * @param email the email to check
     * @return {@code true} if the email does not exist in the database
     * @throws EmailEmptyException if the email is empty
     * @throws InvalidEmailFormatException if the email format is invalid
     * @throws EmailAlreadyInUseException if the email is already registered
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     * @author Andrew Clarke (a40.clarke@connect.edu.au)
     */
    public boolean emailCheck (String email) throws RuntimeException {
        User user;
        if (email.trim().isEmpty()) {
            throw new EmailEmptyException();
        }
        if (!RegexValidator.validEmailAddress(email)) {
            throw new InvalidEmailFormatException(MSG_INVALID_EMAIL_FORMAT);
        }
        try {
            user = userDAO.getUser(email);
            Objects.requireNonNull(user);
        } catch (NullPointerException nullPointerException){
            return true;
        }
        if (Objects.equals(user.getEmail(), email)) {
            throw new EmailAlreadyInUseException();
        }
        return true;
    }

    /**
     * This method registers a new {@link User}
     * <ul>
     *   <li>Check if user's email is not empty and passes format validation</li>
     *   <li>Check if user's password is not empty and passes password requirement validation</li>
     *   <li>Check if user's email is free (using {@link #emailCheck(String)})</li>
     *   <li>Hashing user's password before storing in the database</li>
     * </ul>
     * @param user the {@link User} object containing registration details
     * @return {@code true} if the registration is successful
     * @throws EmailEmptyException if the user's email is empty
     * @throws PasswordEmptyException if the user's password is empty
     * @throws EmailAlreadyInUseException if the email is already registered
     * @throws InvalidEmailFormatException if the email format is invalid
     * @throws InvalidPasswordFormatException if the password fails password requirement
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     * @author Andrew Clarke (a40.clarke@connect.edu.au)
     */
    public boolean register (User user) throws RuntimeException {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new EmailEmptyException(MSG_EMAIL_EMPTY);
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new PasswordEmptyException(MSG_PASSWORD_EMPTY);
        }
        try {
            emailCheck(user.getEmail());
        } catch (EmailAlreadyInUseException emailAlreadyInUseException) {
            throw new EmailAlreadyInUseException(MSG_EMAIL_ALREADY_IN_USE);
        } catch (EmailEmptyException emailEmptyException) {
            throw new EmailEmptyException(MSG_EMAIL_EMPTY);
        }
        if (!RegexValidator.validPassword(user.getPassword())) {
            throw new InvalidPasswordFormatException(MSG_PASSWORD_REQUIREMENT);
        }
        String hashedPassword = PasswordUtils.hashSHA256(user.getPassword());
        user.setPassword(hashedPassword);
        userDAO.addUser(user);
        return true;
    }

    /**
     * This method resets the password for an existing user with the user's email
     * <ul>
     *   <li>Check if email is not empty and in valid format</li>
     *   <li>Check if new password is not empty and meets requirement</li>
     *   <li>Check if user exists (using {@link #emailCheck(String)})</li>
     * </ul>
     * If all checks pass, the user’s new password is hashed and updated in the database.
     * @param email user's email address
     * @param newPassword user's new plain-text password
     * @return {@code true} if password is successfully reset
     * @throws EmailEmptyException if email is empty
     * @throws InvalidEmailFormatException if email does not match the regex
     * @throws PasswordEmptyException if new password is empty
     * @throws InvalidPasswordFormatException if new password fails the regex
     * @throws UserNotFoundException if user does not exist in the database
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     * @author Andrew Clarke (a40.clarke@connect.edu.au)
     */
    public boolean resetPassword (String email, String newPassword) throws RuntimeException {
        if (email == null || email.trim().isEmpty()) {
            throw new EmailEmptyException(MSG_EMAIL_EMPTY);
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new PasswordEmptyException(MSG_PASSWORD_EMPTY);
        }
        boolean result = false;
        try {
            result = emailCheck(email);
        } catch (UserNotFoundException notFoundException) {
            throw new UserNotFoundException();
        } catch (EmailAlreadyInUseException ignored) {}
        if (result) {
            throw new UserNotFoundException();
        }
        if (!RegexValidator.validPassword(newPassword)) {
            throw new InvalidPasswordFormatException(MSG_PASSWORD_REQUIREMENT);
        }
        String hashedPassword = PasswordUtils.hashSHA256(newPassword);
        User user = userDAO.getUser(email);
        user.setPassword(hashedPassword);
        userDAO.updateUser(user);
        return true;
    }

    /**
     * This method verifies that the user’s provided answers to three security questions match
     * the stored answers in the database.
     * <p> Each answer must be non-empty, and the user's email must exist (determined by
     * {@link #emailCheck(String)}). If all answers match, returns {@code true}.
     *
     * @param user the {@link User} whose security questions are being validated
     * @param answerOne the answer for the first security question (non-empty)
     * @param answerTwo the answer for the second security question (non-empty)
     * @param answerThree the answer for the third security question (non-empty)
     * @return {@code true} if all three answers match the stored data
     * @throws EmptyAnswerException if any of the three answers are blank
     * @throws UserNotFoundException if user is not found
     * @throws FailedQuestionException if one or more answers do not match
     * @author Minh Son Doan - Maverick (minhson.doan@connect.qut.edu.au)
     * @author Andrew Clarke (a40.clarke@connect.edu.au)
     */
    public boolean checkSecurityQuestion(User user,String answerOne, String answerTwo, String answerThree) throws RuntimeException {
        if (answerOne == null || answerOne.trim().isEmpty()) {
            throw new EmptyAnswerException(MSG_ANSWER_ONE_EMPTY);
        }
        if (answerTwo == null || answerTwo.trim().isEmpty()) {
            throw new EmptyAnswerException(MSG_ANSWER_TWO_EMPTY);
        }
        if (answerThree == null || answerThree.trim().isEmpty()) {
            throw new EmptyAnswerException(MSG_ANSWER_THREE_EMPTY);
        }
        boolean result = false;
        try {
            result = emailCheck(user.getEmail());
        } catch (RuntimeException ignored) {}
        if (result) {
            throw new UserNotFoundException();
        }
        UserSecurityQuestion usq = userSecurityQuestionDAO.getQuestions(user);
        if (!usq.getAnswerOne().equals(answerOne)) {
            throw new FailedQuestionException(MSG_ANSWER_ONE_MISMATCH);
        }
        if (!usq.getAnswerTwo().equals(answerTwo)) {
            throw new FailedQuestionException(MSG_ANSWER_TWO_MISMATCH);
        }
        if (!usq.getAnswerThree().equals(answerThree)) {
            throw new FailedQuestionException(MSG_ANSWER_THREE_MISMATCH);
        }
        return true;
    }

    public boolean checkSecurityQuestion(User user,String answerOne, String answerTwo) throws RuntimeException {
        if (answerOne == null || answerOne.trim().isEmpty()) {
            throw new EmptyAnswerException(MSG_ANSWER_ONE_EMPTY);
        }
        if (answerTwo == null || answerTwo.trim().isEmpty()) {
            throw new EmptyAnswerException(MSG_ANSWER_TWO_EMPTY);
        }
        boolean result = false;
        try {
            result = emailCheck(user.getEmail());
        } catch (RuntimeException ignored) {}
        if (result) {
            throw new UserNotFoundException();
        }
        UserSecurityQuestion usq = userSecurityQuestionDAO.getQuestions(user);
        if (!usq.getAnswerOne().equals(answerOne)) {
            throw new FailedQuestionException(MSG_ANSWER_ONE_MISMATCH);
        }
        if (!usq.getAnswerTwo().equals(answerTwo)) {
            throw new FailedQuestionException(MSG_ANSWER_TWO_MISMATCH);
        }
        return true;
    }
}
