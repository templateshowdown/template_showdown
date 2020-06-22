package com.example.templateshowdown.object;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class LevelEvent extends RealmObject {
    public static final String PROPERTY_ID = "id";

    @PrimaryKey @Required
    private String id;

    private String level;
    private String eventType;
    private String eventVariableId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventVariableId() {
        return eventVariableId;
    }

    public void setEventVariableId(String eventVariableId) {
        this.eventVariableId = eventVariableId;
    }

}
