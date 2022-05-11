package com.yevhenii.complimentBot.entity;

public class User {

    private String userName;

    private Long chatId;

    public User(String userName, Long chatId) {
        this.userName = userName;
        this.chatId = chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }
}
