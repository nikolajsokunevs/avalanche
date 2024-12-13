package lv.on.avalanche.config.enums;

public enum WebSocketTopics {
    GAME_STATUS("/game/status/");

    private String topic;

    WebSocketTopics(String topic) {
        this.topic = topic;
    }

    public String get(){
        return topic;
    }
}
