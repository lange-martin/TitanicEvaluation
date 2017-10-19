package hello;

import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class Evaluator {

    private String path;

    public Statistic[] statistic = new Statistic[9];

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void run() throws IOException {
        //Build reader instance
        //Read data.csv
        //Default seperator is comma
        //Default quote character is double quote
        //Start reading from first line (line numbers start from zero)
        CSVReader reader = new CSVReader(new FileReader("upload-dir/" + path), ',' , '"' , 0);

        //Read CSV line by line and use the string array as you want
        String[] nextLine;

        for (int i = 0; i <= 8; i++) {
            statistic[i] = new Statistic();
            // 0: all (Crew + Passengers); 1-3: classes; 4-7: age groups (twenty years); 8: crew
        }

        while ((nextLine = reader.readNext()) != null) {
            if (isReadablePassenger(nextLine)) {

                int age;
                boolean death = false;

                // Age
                age = Integer.parseInt(nextLine[2].trim()); // age is in the third column
                statistic[0].addAgeToTotal(age, nextLine[1]+" "+nextLine[0]); // age added to total and passes name of passenger in case they are the oldest
                statistic[0].addPassenger(); // passenger count increased by one

                // Deaths
                if (nextLine.length >= 5) { // this line has at least 5 columns
                    if (nextLine[4].length() > 0) { // column 5 is not empty
                        statistic[0].addDeadPassenger();
                        death = true;
                    }
                }
                // add data to respective class (or crew)
                if (nextLine.length >=4) {
                    String classCell = nextLine[3].trim();
                    classCell = classCell.substring(0, 1); // takes first character of class cell (should be digit)
                    int arrayField;
                    switch (classCell) {
                        case "1": arrayField = 1;
                            break;
                        case "2": arrayField = 2;
                            break;
                        case "3": arrayField = 3;
                            break;
                        default: arrayField = 8; // if first character is no digit -> passenger must be part of crew
                            break;
                    }
                    statistic[arrayField].addAgeToTotal(age, nextLine[1]+" "+nextLine[0]);
                    statistic[arrayField].addPassenger();
                    if (death) {
                        statistic[arrayField].addDeadPassenger();
                    }
                }

                // add data to respective age group
                if (age < 20) {
                    statistic[4].addAgeToTotal(age, nextLine[1]+" "+nextLine[0]);
                    statistic[4].addPassenger();
                    if (death) {
                        statistic[4].addDeadPassenger();
                    }
                } else if (age < 40) {
                    statistic[5].addAgeToTotal(age, nextLine[1]+" "+nextLine[0]);
                    statistic[5].addPassenger();
                    if (death) {
                        statistic[5].addDeadPassenger();
                    }
                } else if (age < 60) {
                    statistic[6].addAgeToTotal(age, nextLine[1]+" "+nextLine[0]);
                    statistic[6].addPassenger();
                    if (death) {
                        statistic[6].addDeadPassenger();
                    }
                } else if (age < 80) {
                    statistic[7].addAgeToTotal(age, nextLine[1]+" "+nextLine[0]);
                    statistic[7].addPassenger();
                    if (death) {
                        statistic[7].addDeadPassenger();
                    }
                }
            }
        }
    }

    public String report(int group) { // String with data for one of the statistic objects
        String output = "";
        output += "Passengers: " + statistic[group].getPassengers() + " | ";
        output += "Average Age: " + statistic[group].getAverageAge() + " | ";
        output += "Oldest Person: " + statistic[group].getOldestName() + ", " + statistic[group].getOldest() + " | ";
        output += "Deaths: " + statistic[group].getPassengersDead() + " | ";
        output += "Death Percentage: " + statistic[group].getDeathPercentage();
        return output;
    }

    public int getValue(int group, int value) {
        switch (value) {
            case 0: return (int) statistic[group].getPassengers();
            case 1: return (int) statistic[group].getAverageAge();
            case 2: return (int) statistic[group].getOldest();
            case 3: return (int) statistic[group].getPassengersDead();
            case 4: return (int) statistic[group].getDeathPercentage();
            default: return 0;
        }
    }

    private static boolean isReadablePassenger(String[] line) { // true if this line is formatted correctly for this reader
        if (line.length > 3) { // must have more than 3 columns (surname, forename, age, class, death (optional))
            try { // this bit is hacky: checks if column 3 includes only a number (age)
                int a = Integer.parseInt(line[2].trim()); // no exception -> return true
                return true;
            } catch (NumberFormatException e) { // parseInt will throw exception if it's not a number -> return false
                return false;
            }
        } else {
            return false;
        }
    }
}
