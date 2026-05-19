package model;

import enums.LessonType;
import java.io.Serializable;

public class Lesson implements Serializable {
    private String topic;
    private LessonType lessonType;
    private String day;
    private String time;
    private String room;

    public Lesson(String topic, LessonType lessonType, String day, String time, String room) {
        this.topic = topic;
        this.lessonType = lessonType;
        this.day = day;
        this.time = time;
        this.room = room;
    }

    public String getTopic() {
        return topic;
    }

    public LessonType getLessonType() {
        return lessonType;
    }

    public String getDay() {
        return day;
    }

    public String getTime() {
        return time;
    }

    public String getRoom() {
        return room;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setLessonType(LessonType lessonType) {
        this.lessonType = lessonType;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return lessonType + " | " + topic + " | " + day + " " + time + " | Room: " + room;
    }
}