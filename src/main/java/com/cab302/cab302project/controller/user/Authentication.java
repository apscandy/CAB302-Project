package com.cab302.cab302project.controller.user;

import com.cab302.cab302project.ApplicationState;
import com.cab302.cab302project.error.UserAlreadyLoggedInException;
import com.cab302.cab302project.error.authenicaton.*;
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

public class Authentication {

    private static final Logger logger = LogManager.getLogger(Authentication.class);

    private IUserDAO userDAO;
    private IUserSecurityQuestionDAO userSecurityQuestionDAO;

    public Authentication() {
        this.userDAO = new SqliteUserDAO();
        this.userSecurityQuestionDAO = new SqliteUserSecurityQuestionDAO();
    }

    public boolean authenticate(String email, String password) throws RuntimeException {
        if (ApplicationState.isUserLoggedIn()){
            logger.info("User is already logged in");
            throw new UserAlreadyLoggedInException();
        }
        if (password == null || password.trim().isEmpty()) {
            logger.warn("Authentication failed: email or password is empty");
            throw new PasswordEmptyException("password is empty");
        }
        if (email == null || email.trim().isEmpty()) {
            logger.warn("Authentication failed: email is empty");
            throw new EmailEmptyException("email is empty");
        }
        try {
            emailCheck(email);
        } catch (EmailAlreadyInUseException ignored){}
        User user = userDAO.getUser(email);
        if (user == null) {
            logger.warn("Authentication failed: user not found");
            throw new UserNotFoundException("user not found");
        }
        String userPassword = user.getPassword();
        String hashedPassword = PasswordUtils.hashSHA256(password);
        if (!userPassword.equals(hashedPassword)) {
            logger.warn("Authentication failed: incorrect password for email");
            throw new PasswordComparisonException("incorrect password provided");
        }
        ApplicationState.login(user);
        return true;
    }

    public boolean emailCheck (String email) throws RuntimeException {
        User user;
        if (email.trim().isEmpty()) {
            logger.warn("Email check failed: empty email provided");
            throw new EmailEmptyException();
        }
        if (!RegexValidator.validEmailAddress(email)) {
            logger.warn("Invalid email format provided");
            throw new InvalidEmailFormatException("invalid email format");
        }
        try {
            user = userDAO.getUser(email);
            Objects.requireNonNull(user);
        } catch (NullPointerException nullPointerException){
            return true;
        }
        if (Objects.equals(user.getEmail(), email)) {
            logger.warn("Email check failed: user not found");
            throw new EmailAlreadyInUseException();
        }
        return true;
    }

    public boolean register (User user) throws RuntimeException {

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            logger.warn("Registration failed: user is email empty");
            throw new EmailEmptyException("email is empty");
        }
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            logger.warn("Registration failed: password is empty");
            throw new PasswordEmptyException("password is empty");
        }
        try {
            emailCheck(user.getEmail());
        } catch (EmailAlreadyInUseException emailAlreadyInUseException) {
            logger.info("Email already in use");
            throw new EmailAlreadyInUseException("Email already in use");
        } catch (EmailEmptyException emailEmptyException) {
            logger.info("Email empty");
            throw new EmailEmptyException("Email empty");
        }
        if (!RegexValidator.validPassword(user.getPassword())) {
            logger.warn("Registration: Password provided did not meet the requirement");
            throw new InvalidPasswordFormatException("password did not meet the requirement");
        }
        String hashedPassword = PasswordUtils.hashSHA256(user.getPassword());
        user.setPassword(hashedPassword);
        userDAO.addUser(user);
        return true;
    }


    public boolean resetPassword (String email, String newPassword) throws RuntimeException {
        if (email == null || email.trim().isEmpty()) {
            logger.warn("Reset password failed: email is empty");
            throw new EmailEmptyException("email is empty");
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
            logger.warn("Reset password failed: password is empty");
            throw new PasswordEmptyException("password is empty");
        }
        boolean result = false;
        try {
            result = emailCheck(email);
        } catch (UserNotFoundException notFoundException) {
            logger.warn("Reset password failed: user not found");
            throw new UserNotFoundException();
        } catch (EmailAlreadyInUseException ignored) {}
        if (result) {
            logger.warn("Reset password failed: user not found");
            throw new UserNotFoundException();
        }
        if (!RegexValidator.validPassword(newPassword)) {
            logger.warn("Password Reset: New password provided did not meet the requirement");
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
            logger.warn("Security question check failed: answerOne is empty");
            throw new EmptyAnswerException("answerOne is empty");
        }
        if (answerTwo == null || answerTwo.trim().isEmpty()) {
            logger.warn("Security question check failed: answerTwo is empty");
            throw new EmptyAnswerException("answerTwo is empty");
        }
        if (answerThree == null || answerThree.trim().isEmpty()) {
            logger.warn("Security question check failed: answerThree is empty");
            throw new EmptyAnswerException("answerThree is empty");
        }
        boolean result = false;
        try {
            result = emailCheck(user.getEmail());
        } catch (RuntimeException ignored) {}
        if (result) {
            logger.warn("Security question check failed: user not found");
            throw new UserNotFoundException();
        }
        UserSecurityQuestion usq = userSecurityQuestionDAO.getQuestions(user);
        if (!usq.getAnswerOne().equals(answerOne)) {
            logger.warn("Check security question failed: answerOne does not match");
            throw new FailedQuestionException("Answer one does not match");
        }
        if (!usq.getAnswerTwo().equals(answerTwo)) {
            logger.warn("Check security question failed: answerTwo does not match");
            throw new FailedQuestionException("Answer two does not match");
        }
        if (!usq.getAnswerThree().equals(answerThree)) {
            logger.warn("Check security question failed: answerThree does not match");
            throw new FailedQuestionException("Answer three does not match");
        }
        return true;
    }
}
