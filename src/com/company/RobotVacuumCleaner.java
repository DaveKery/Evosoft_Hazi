package com.company;
import java.time.LocalDate;
import java.util.ArrayList;

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
