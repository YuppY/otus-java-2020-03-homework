package ru.otus.hw.webserver.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
