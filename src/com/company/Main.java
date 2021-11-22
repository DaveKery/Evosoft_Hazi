package com.company;
import org.postgresql.util.PSQLException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalTime;

interface VacuumCleanerFunctions {
    public void startMachine(int NoOfSteps, int[] catInTheRoom);
}

public class Main extends RobotVacuumCleaner {

    private static boolean withoutError = true;
    public static int[] catInTheRoom = {1,1};   // 1st value: column, 2nd value: row (range of these two values must be between 1-5, 0-0 means cat will not bother the vacuum cleaner at all)

    public static void main(String[] args) {

        RobotVacuumCleaner vacuum1 = new RobotVacuumCleaner(1,1);
        vacuum1.getRoomStatus();
        vacuum1.setNumberOfSteps(getNumberOfDirtySpots());  // setting the number of steps depending on how many dirty spots are there in the room

        try{
            if(vacuum1.getNumberOfSteps() > 25 || vacuum1.getNumberOfSteps() < 1){  // number of steps cannot be larger than 25 since the size of room is 25
                errorDate = LocalDate.now();  // error date moment
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

    /* @method name: getConnection
     * @param: ---
     * @return: null
     * @description: It is used for storing exception data caught during working process */
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
            System.out.println("Storing ERROR data into PostgreSQL database has completed successfully!");

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