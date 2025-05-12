package com.cab302.cab302project.model.session;

import java.time.LocalDateTime;

public interface ISessionDAO {
    void createSession(Session session);
    void endSession(Session session);

    void createSessionResult(SessionResults sessionResults);
    void sessionResultCardCorrect(SessionResults sessionResults, LocalDateTime startTime, LocalDateTime endTime);
    void sessionResultCardIncorrect(SessionResults sessionResults, LocalDateTime startTime, LocalDateTime endTime);

}
