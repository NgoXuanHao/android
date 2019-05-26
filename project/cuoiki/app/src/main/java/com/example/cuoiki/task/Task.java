package com.example.cuoiki.task;

import java.util.ArrayList;

public class Task {
    private int id;
    private String title;
    private long date_start;
    private long date_end;
    private boolean done;
    private long created;
    private boolean notify;
    private int count;
    private int total;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Task(int id, String title, long date_start, long date_end, boolean done, long created, boolean notify) {
        this.id = id;
        this.title = title;
        this.date_start = date_start;
        this.date_end = date_end;
        this.done = done;
        this.created = created;
        this.notify = notify;
    }

    public Task(String title, long date_start, long date_end, boolean done, long created, boolean notify) {
        this.title = title;
        this.date_start = date_start;
        this.date_end = date_end;
        this.done = done;
        this.created = created;
        this.notify = notify;
    }

    public Task() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getDate_start() {
        return date_start;
    }

    public void setDate_start(long date_start) {
        this.date_start = date_start;
    }

    public long getDate_end() {
        return date_end;
    }

    public void setDate_end(long date_end) {
        this.date_end = date_end;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }
}
