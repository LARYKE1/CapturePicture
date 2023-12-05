package com.example.capturepicture;

import android.nfc.Tag;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class ImageModel {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "Path Location")
    private String imageCaptured;

    @ColumnInfo(name = "Tag")
    private String tag;

    public ImageModel(String imageCaptured, String tag) {
        this.imageCaptured = imageCaptured;
        this.tag = tag;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getImageCaptured() {
        return imageCaptured;
    }

    public void setImageCaptured(String imageCaptured) {
        this.imageCaptured = imageCaptured;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
