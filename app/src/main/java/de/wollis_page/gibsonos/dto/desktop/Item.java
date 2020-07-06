package de.wollis_page.gibsonos.dto.desktop;

import androidx.annotation.NonNull;

public class Item
{
    @NonNull
    private String module;
    @NonNull
    private String task;
    @NonNull
    private String action;
    private String text;
    private String icon;
    private String thumb;
    private String customIcon;
    //public final JSONObject params;

    public Item(
        @NonNull String module,
        @NonNull String task,
        @NonNull String action,
        String text,
        String icon,
        String thumb,
        String customIcon//,
        //JSONObject params
    ) {
        this.module = module;
        this.task = task;
        this.action = action;
        this.text = text;
        this.icon = icon;
        this.thumb = thumb;
        this.customIcon = customIcon;
        //this.params = params;
    }

    @NonNull
    public String getModule() {
        return module;
    }

    public void setModule(@NonNull String module) {
        this.module = module;
    }

    @NonNull
    public String getTask() {
        return task;
    }

    public void setTask(@NonNull String task) {
        this.task = task;
    }

    @NonNull
    public String getAction() {
        return action;
    }

    public void setAction(@NonNull String action) {
        this.action = action;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getCustomIcon() {
        return customIcon;
    }

    public void setCustomIcon(String customIcon) {
        this.customIcon = customIcon;
    }
}
