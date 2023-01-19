package me.ranzeplay.instantmarker.models;

public class BroadcastItem {
    private String translationKey;
    private int count;

    public BroadcastItem(String translationKey, int count) {
        this.translationKey = translationKey;
        this.count = count;
    }

    public String getTranslationKey() {
        return translationKey;
    }

    public int getCount() {
        return count;
    }
}
