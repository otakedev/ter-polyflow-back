package fr.polytech.email.components.content;

public interface ContentBuilder {
    ContentBuilder init(String template);
    ContentBuilder put(String key, Object value);
    String render();
    String createDefault(String content);
}
