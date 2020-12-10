package fr.polytech.email.components.content;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Component
public class ContentBean implements ContentBuilder {

    private class Content {

        private Map<String, Object> model = new HashMap<String, Object>();
        private Context context = new Context();
        private String template;

        private Content put(String key, Object value) {
            model.put(key, value);
            return this;
        }

        private String render(SpringTemplateEngine templateEngine) {
            context.setVariables(model);
            String html = templateEngine.process(template, context);
            return html;
        }

        private void setTemplate(String template) {
            this.template = template;
        }
    }

    private final static String DEFAULT_TEMPLATE = "default";
    private Content content = new Content();

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public ContentBuilder init(String template) {
        content.setTemplate(template);
        return this;
    }

    @Override
    public ContentBuilder put(String key, Object value) {
        content.put(key, value);
        return this;
    }

    @Override
    public String render() {
        return content.render(templateEngine);
    }

    @Override
    public String createDefault(String content) {
        this.init(DEFAULT_TEMPLATE);
        this.put("content", content);
        return this.render();
    }

}
