package io.github.innobridge.llamademo.model;

import java.util.List;

public class ChatRequest {
    private String sessionId;
    private List<ChatMessage> messages;

    public ChatRequest() {
    }

    public ChatRequest(String sessionId, List<ChatMessage> messages) {
        this.sessionId = sessionId;
        this.messages = messages;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
