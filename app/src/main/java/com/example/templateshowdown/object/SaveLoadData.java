package com.example.templateshowdown.object;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SaveLoadData extends Application { //using file io stream for now, will update to better method for server friendliness in the future
    public static User userData;
    public static User database;
    public static TempVariable tempData;
    public void saveUser(User userData, Context test) throws IOException  {
        File saveData = new File(test.getFilesDir(),"userData.txt");
        this.userData = userData;
        FileOutputStream fileOutputStream = new FileOutputStream(saveData);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(userData);
    }
    public static User loadUser(Context test) throws IOException, ClassNotFoundException {
        File saveData = new File(test.getFilesDir(),"userData.txt");
        FileInputStream fileInputStream = new FileInputStream(saveData);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        userData = (User)objectInputStream.readObject();
        return userData;
    }
    public void saveDatabase(User userData, Context test) throws IOException  {
        File saveData = new File(test.getFilesDir(),"database.txt");
        this.database = userData;
        FileOutputStream fileOutputStream = new FileOutputStream(saveData);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(userData);
    }
    public static User loadDatabase(Context test) throws IOException, ClassNotFoundException {
        File saveData = new File(test.getFilesDir(),"database.txt");
        FileInputStream fileInputStream = new FileInputStream(saveData);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        database = (User)objectInputStream.readObject();
        return database;
    }
}
