package at.htl.ecopoints.model;

public class CardContent {
    private String value;
    private String description;
    private int icon;

    public CardContent() {
    }

    public CardContent(String value, String description, int icon) {
        this.value = value;
        this.description = description;
        this.icon = icon;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
