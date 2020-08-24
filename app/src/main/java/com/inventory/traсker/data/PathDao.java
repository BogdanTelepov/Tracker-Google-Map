package com.inventory.traсker.data;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;



@Dao
public interface PathDao {

    @Insert
    void insert(Path path);


    @Query("SELECT * FROM path_table WHERE id=:id")
    Path getPath(int id);

    @Query("SELECT * FROM path_table")
    Path getAllPath();
}
