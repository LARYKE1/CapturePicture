package com.example.capturepicture;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ModelDao {

    @Query("SELECT *FROM imagemodel")
    List<ImageModel>getAll();

    @Query("SELECT * FROM imagemodel WHERE Tag IN (:tags)")
    List<ImageModel> image(List<String> tags);

    @Insert
        void insertData(ImageModel... models);

    @Delete
        void deleteAll(List<ImageModel> models);

}
