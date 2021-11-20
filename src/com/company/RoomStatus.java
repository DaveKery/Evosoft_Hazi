package com.company;
import java.util.ArrayList;

class RoomStatus {

    public static ArrayList<String> roomStatus = new ArrayList<>();
    private static int dirtySpots = 0;

    RoomStatus(){
        checkCleannessInRoom();
    }

    /* @method name: checkCleannessInRoom
     * @param: ---
     * @return: ---
     * @description: It is used for randomize a number for clean and dirty spots of the room and storing them in an ArrayList */
    private void checkCleannessInRoom(){

        int NoOfcleanSpot = 0;


        while(true){
            NoOfcleanSpot = (int) (Math.random() * 25);
            if(NoOfcleanSpot > 0) break;
        }

        for(int i = 0; i < NoOfcleanSpot; i++){
            roomStatus.add("CLEAN");
        }
        for(int i = 0; i < 25-NoOfcleanSpot; i++){
            roomStatus.add("DIRT");
        }
    }

    /* @method name: getNumberOfDirtySpots
     * @param: ---
     * @return: dirtySpots
     * @description: It is used for state the status of room */
    public static int getNumberOfDirtySpots(){

        for(int i = 0; i < roomStatus.size(); i++){
            if(roomStatus.get(i) == "DIRT"){
                dirtySpots++;
            }
        }
        System.out.println(dirtySpots + " dirty spots are there in the room!");
        return dirtySpots;
    }
}