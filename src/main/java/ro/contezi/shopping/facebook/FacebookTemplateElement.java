package ro.contezi.shopping.facebook;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FacebookTemplateElement {
    private String title;
    private List<FacebookUrlButton> buttons = new ArrayList<>();

    public List<FacebookUrlButton> getButtons() {
        return Collections.unmodifiableList(buttons);
    }

    public void setButtons(List<FacebookUrlButton> buttons) {
        this.buttons = new ArrayList<>(buttons);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
