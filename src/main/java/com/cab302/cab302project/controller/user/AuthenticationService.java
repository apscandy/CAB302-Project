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

public final class AuthenticationService {

    private static final Logger logger = LogManager.getLogger(AuthenticationService.class);

    private final IUserDAO userDAO;
    private final IUserSecurityQuestionDAO userSecurityQuestionDAO;

    public AuthenticationService() {
        this.userDAO = new SqliteUserDAO();
        this.userSecurityQuestionDAO = new SqliteUserSecurityQuestionDAO();
    }

    public boolean authenticate(String email, String password) throws RuntimeException {
        if (ApplicationState.isUserLoggedIn()){
            throw new UserAlreadyLoggedInException();
        }
        if (password == null || password.trim().isEmpty()) {
            throw new PasswordEmptyException("password is empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new EmailEmptyException("email is empty");
        }
        try {
            emailCheck(email);
        } catch (EmailAlreadyInUseException ignored){}
        User user = userDAO.getUser(email);
        if (user == null) {
            throw new UserNotFoundException("user not found");
        }
        String userPassword = user.getPassword();
        String hashedPassword = PasswordUtils.hashSHA256(password);
        if (!userPassword.equals(hashedPassword)) {
            throw new PasswordComparisonException("incorrect password provided");
        }
        ApplicationState.login(user);
        return true;
    }

    /**
     * This function in a rather confusing manner returns true
     * if the email is not found in the database
     * @author Andrew Clarke (a40.clarke@connect.edu.au)
     * @author Maverick Doan
     * @param email
     * @return
     * @throws RuntimeException
     */
    public boolean emailCheck (String email) throws RuntimeException {
        User user;
        if (email.trim().isEmpty()) {
            throw new EmailEmptyException();
        }
        if (!RegexValidator.validEmailAddress(email)) {
            throw new InvalidEmailFormatException("invalid email format");
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

    public boolean register (User user) throws RuntimeException {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new EmailEmptyException("email is empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new PasswordEmptyException("password is empty");
        }
        try {
            emailCheck(user.getEmail());
        } catch (EmailAlreadyInUseException emailAlreadyInUseException) {
            throw new EmailAlreadyInUseException("Email already in use");
        } catch (EmailEmptyException emailEmptyException) {
            throw new EmailEmptyException("Email empty");
        }
        if (!RegexValidator.validPassword(user.getPassword())) {
            throw new InvalidPasswordFormatException("password did not meet the requirement");
        }
        String hashedPassword = PasswordUtils.hashSHA256(user.getPassword());
        user.setPassword(hashedPassword);
        userDAO.addUser(user);
        return true;
    }

    public boolean resetPassword (String email, String newPassword) throws RuntimeException {
        if (email == null || email.trim().isEmpty()) {
            throw new EmailEmptyException("email is empty");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            throw new PasswordEmptyException("password is empty");
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
            throw new InvalidPasswordFormatException("password did not meet the requirement");
        }
        String hashedPassword = PasswordUtils.hashSHA256(newPassword);
        User user = userDAO.getUser(email);
        user.setPassword(hashedPassword);
        userDAO.updateUser(user);
        return true;
    }

    public boolean checkSecurityQuestion(User user,String answerOne, String answerTwo, String answerThree) throws RuntimeException {
        if (answerOne == null || answerOne.trim().isEmpty()) {
            throw new EmptyAnswerException("answerOne is empty");
        }
        if (answerTwo == null || answerTwo.trim().isEmpty()) {
            throw new EmptyAnswerException("answerTwo is empty");
        }
        if (answerThree == null || answerThree.trim().isEmpty()) {
            throw new EmptyAnswerException("answerThree is empty");
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
            throw new FailedQuestionException("Answer one does not match");
        }
        if (!usq.getAnswerTwo().equals(answerTwo)) {
            throw new FailedQuestionException("Answer two does not match");
        }
        if (!usq.getAnswerThree().equals(answerThree)) {
            throw new FailedQuestionException("Answer three does not match");
        }
        return true;
    }

    public boolean checkSecurityQuestion(User user,String answerOne, String answerTwo) throws RuntimeException {
        if (answerOne == null || answerOne.trim().isEmpty()) {
            throw new EmptyAnswerException("answerOne is empty");
        }
        if (answerTwo == null || answerTwo.trim().isEmpty()) {
            throw new EmptyAnswerException("answerTwo is empty");
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
            throw new FailedQuestionException("Answer one does not match");
        }
        if (!usq.getAnswerTwo().equals(answerTwo)) {
            throw new FailedQuestionException("Answer two does not match");
        }
        return true;
    }
}
