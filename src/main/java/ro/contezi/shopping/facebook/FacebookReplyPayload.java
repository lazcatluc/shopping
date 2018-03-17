package ro.contezi.shopping.facebook;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FacebookReplyPayload {
    @JsonProperty("template_type")
    private FacebookTemplateType templateType = FacebookTemplateType.GENERIC;
    @JsonProperty("top_element_style")
    private String topElementStyle;
    private String text;
    private List<FacebookTemplateElement> elements;

    public FacebookTemplateType getTemplateType() {
        return templateType;
    }

    public void setTemplateType(FacebookTemplateType templateType) {
        this.templateType = templateType;
    }

    public String getTopElementStyle() {
        return topElementStyle;
    }

    public void setTopElementStyle(String topElementStyle) {
        this.topElementStyle = topElementStyle;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setButtons(List<FacebookUrlButton> buttons) {
        FacebookTemplateElement element = new FacebookTemplateElement();
        element.setButtons(buttons);
        element.setTitle(buttons.get(0).getTitle());
        elements = Collections.singletonList(element);
    }

    public List<FacebookTemplateElement> getElements() {
        return Collections.unmodifiableList(elements);
    }

    public void setElements(List<FacebookTemplateElement> elements) {
        this.elements = new ArrayList<>(elements);
    }
}
