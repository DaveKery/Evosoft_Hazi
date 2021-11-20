package com.company;
import org.postgresql.util.PSQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.ArrayList;

interface VacuumCleanerFunctions {
    public void startMachine(int NoOfSteps, int[] catInTheRoom);
}

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

class RobotVacuumCleaner extends RoomStatus implements VacuumCleanerFunctions {

    private int vacuumColPos;
    private int vacuumRowPos;
    private int NumberOfSteps;
    static LocalDate errorDate;

    RobotVacuumCleaner(){       // Constructor is used for setting the initial position of vacuum cleaner (by default it is: col=1, row=1)
        this.vacuumColPos = 1;
        this.vacuumRowPos = 1;
    }

    RobotVacuumCleaner(int initialCol, int initialRow){       // Constructor is used for setting the initial position of vacuum cleaner
        this.vacuumColPos = initialCol;
        this.vacuumRowPos = initialRow;
    }

    /* @method name: setNumberOfSteps
    *  @param: steps
    *  @return: ---
    *  @description: It is used for setting number of steps that vacuum cleaner will take in the room */
    public void setNumberOfSteps(int steps){
        this.NumberOfSteps = steps;
    }

    /* @method name: getNumberOfSteps
     * @param: ---
     * @return: NumberOfSteps
     * @description: It is used for returning the number of given steps */
    public int getNumberOfSteps(){
        return NumberOfSteps;
    }

    /* @method name: getRoomStatus
     * @param: ---
     * @return: roomStatus
     * @description: It is used for state how dirty is the room, checked by the elements of 'roomStatus' */
    public ArrayList<String> getRoomStatus(){

        int dirtCounter = 0;

        for(String s : roomStatus){ // iterate through the inherited 'roomStatus' ArrayList
            if(s == "DIRT"){
                dirtCounter++;
            }
        }

        if(dirtCounter == 0){
            System.out.println("Room is cleaned everywhere!");
        }
        else if(dirtCounter < roomStatus.size()/2 && dirtCounter > 0){
            System.out.println("Room is need to be cleaned a bit!");
        }else if(dirtCounter >= roomStatus.size()/2){
            System.out.println("Room is dirty everywhere, just call the housekeeper for service!");
        }
        return roomStatus;
    }

    /* @method name: moveVacuumCleaner
     * @param: vacuumColPos, vacuumRowPos, catInTheRoom
     * @return: ---
     * @description: It is used for showing a movement of machine with the room walls around it */
    private void moveVacuumCleaner(int vacuumColPos, int vacuumRowPos, int[] catInTheRoom){

        int n = 1;  // '1' presents the wall
        boolean vacuumMOVED = false;

        for(int row = 0; row < 7; row++){  // number of row to make a room with the actual position of machine

            if(row == 0 || row == 6){   // first and last row must be fully filled with walls

                for(int col = 0; col < 7; col++){     // number of column
                    System.out.print(n + " ");
                }
                System.out.println();   // move on to the next row
            }else{

                for(int col = 0; col < 7; col++){   // number of column
                    if(col == 0 || col == 6){       // just the right most and left most side must be the wall
                        System.out.print(n + " ");
                    }

                    else if(col != 0 && col != 6 && vacuumMOVED == false){     // if vacuum is not printed out yet and column is not at the wall sides

                        if(col == vacuumColPos && row == vacuumRowPos){

                            if(catInTheRoom[0] == col && catInTheRoom[1] == row){

                                errorDate = LocalDate.now();  // error moment when a cat blocks the machine
                                System.out.println();
                                throw new RuntimeException("ERROR: Cat blocks the road at row:" + catInTheRoom[1] + " and column:" + catInTheRoom[0]);      // throw an ERROR and stops the whole process
                            }

                            System.out.print("V" + " "); // printing the actual position of vacuum by using 'V' letter
                            vacuumMOVED = true;       // !!! to be sure it must be printed only one time + col and row values will be stored once !!!
                        }else{
                            System.out.print("  "); // put invisible things where there is no vacuum cleaner currently
                        }
                    }else{
                        System.out.print("  "); // put invisible things where there is no vacuum cleaner currently
                    }
                }
                System.out.println();   // move on to the next row
            }
        }
    }

    /* @method name: startMachine
     * @param: NoOfSteps, catInTheRoom
     * @return: ---
     * @description: It is used for determining the direction and using 'moveVacuumCleaner' method to take steps by calling it */
    public void startMachine(int NoOfSteps, int[] catInTheRoom){

        int actualValueInLine = 1;
        boolean forward = true;         // vacuum starts to move forward by default
        boolean backward = false;    // vacuum starts to move forward by default and not in backward direction
        int NoOfRun = 0;

        for(int n = 0; n < NoOfSteps; n++){        // run 'moveVacuumCleaner' method 'NoOfSteps' times -> 'NoOfSteps' is the given number of steps of vacuum cleaner in the room

            moveVacuumCleaner(vacuumColPos, vacuumRowPos, catInTheRoom);

            try{
                Thread.sleep(1000);     // ~1000ms delay
            }catch (InterruptedException e){
                System.out.println(e);
            }

            NoOfRun++;
            System.out.println("Number of taken steps: " + NoOfRun + "\n");   // separate moments of movement from each other

            // * * * FORWARD direction * * * //
            if(actualValueInLine < 5 && forward == true){    // if col is not wall, then increment vacuum column
                actualValueInLine++;
                vacuumColPos++;
            }else if(actualValueInLine == 5 && forward == true){    // CHANGE TO BACKWARD DIRECTION
                vacuumColPos = 5;   // start from the end of row
                vacuumRowPos++;     // start in a new line
                forward = false;    // disable FORWARD direction
                backward = true;    // enable BACKWARD direction
            }
            // * * * BACKWARD direction * * * //
            else if(actualValueInLine != 1 && backward == true){
                actualValueInLine--;
                vacuumColPos--;
            }else if(actualValueInLine == 1 && backward == true){ // CHANGE TO FORWARD DIRECTION
                forward = true;     // enable FORWARD direction
                backward = false;   // disable BACKWARD direction
                vacuumColPos = 1;   // start from the beginning of row
                vacuumRowPos++;     // start in a new line
            }
        }
    }

}

public class Main extends RobotVacuumCleaner {

    private static boolean withoutError = true;
    public static int[] catInTheRoom = {2,1};   // 1st value: column, 2nd value: row (range of these two values must be between 1-5, 0-0 means cat will not bother the vacuum cleaner at all)

    public static void main(String[] args) {

        RobotVacuumCleaner vacuum1 = new RobotVacuumCleaner(1,1);
        vacuum1.getRoomStatus();
        vacuum1.setNumberOfSteps(getNumberOfDirtySpots());  // setting the number of steps depending on how many dirty spots are there in the room

        try{
            if(vacuum1.getNumberOfSteps() > 25 || vacuum1.getNumberOfSteps() < 1){  // number of steps cannot be larger than 25 since the size of room is 25
                errorDate = LocalDate.now();  // error moment
                throw new ArithmeticException("ERROR");
            }
            vacuum1.startMachine(vacuum1.getNumberOfSteps(), catInTheRoom);   // start the vacuum cleaner machine
        }catch (ArithmeticException ae){
            System.out.println(ae);
            withoutError = false;
            getConnection();
        }catch (Exception e){
            System.out.println(e);
            withoutError = false;
            getConnection();
        }finally {
            if(withoutError == true) System.out.println("Cleaning process has run successfully!");
            if(withoutError == false) System.out.println("Vacuum Cleaner machine got en error during in process!");
        }
    }

    public static Connection getConnection() {

        try{
            Class.forName("org.postgresql.Driver");
            System.out.println("Driver O.K.");

            String url = "jdbc:postgresql://localhost:5432/";
            String userName = "postgres";
            String password = "*******";

            Connection conn = DriverManager.getConnection(url, userName, password);
            System.out.println("Successfully connected to PostgreSQL database!");

            String query = "insert into Evosoft_hazi(errortime) values(\'" + errorDate + "\');";

            PreparedStatement preparedStmt = conn.prepareStatement(query);  // sending SQL statements to the database
            preparedStmt.execute();

            conn.close();

        }catch (PSQLException psqlEx){
            System.out.println(psqlEx);
        }
        catch (Exception e){
            System.out.println(e);
        }
        return null;
    }
}

/* iml előző tartalma:
<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4">
  <component name="NewModuleRootManager" inherit-compiler-output="true">
    <exclude-output />
    <content url="file://$MODULE_DIR$">
      <sourceFolder url="file://$MODULE_DIR$/src" isTestSource="false" />
    </content>
    <orderEntry type="inheritedJdk" />
    <orderEntry type="sourceFolder" forTests="false" />
  </component>
</module>
* */