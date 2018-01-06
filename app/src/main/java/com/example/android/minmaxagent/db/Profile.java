package com.example.android.minmaxagent.db;

/**
 * Created by Rohit on 1/3/2018.
 */

public class Profile {
    String name,password;
    int fruitType, gridSize;

    public Profile() {
    }

    public Profile(String name, String password, int fruitType, int gridSize) {
        this.name = name;
        this.password = password;
        this.fruitType = fruitType;
        this.gridSize = gridSize;
    }

    public String getName() {

        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getFruitType() {
        return fruitType;
    }

    public void setFruitType(int fruitType) {
        this.fruitType = fruitType;
    }

    public int getGridSize() {
        return gridSize;
    }

    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }

    public void setName(String name) {
        this.name = name;

    }

    @Override
    public String toString() {
        return this.name + " - " + this.password + " - " + this.fruitType + " - " + this.gridSize;
    }


}
