package hello;

import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class Evaluator {

    private String path; // filename
    private boolean success; // true if the application was able to evaluate data of the file (important for evaluationResultTest.html)
    private Statistic[] statistic = new Statistic[11]; // every cell of this array is one group (crew, age groups, classes, ...)

    public String getPath() {
        return path;
    }
    public boolean getSuccess() {
        return success;
    }
    void setPath(String path) {
        this.path = path;
    }

    void run() throws IOException { // called by EvaluationController.java - this kicks of the evaluation
        CSVReader reader = new CSVReader(new FileReader("upload-dir/" + path), ',', '"', 0); // I use openCSV to read the csv file line by line

        String[] nextLine; // this String array always contains all the cells of one line
        success = false;
        boolean foundFormat = false; // used to end the following while-loop

        while ((nextLine = reader.readNext()) != null && !foundFormat) { // read as long as the next line is not null (end of document) and no fitting format has been found
            if (readableLineFormatOne(nextLine)) { // if this line fits format 1 -> evaluate using this format
                formatOne();
                success = true;
                foundFormat = true;
            } else if (readableLineFormatTwo(nextLine)) { // if it fits format 2 -> use that one
                formatTwo();
                success = true;
                foundFormat = true;
            }
        } // continue until a fitting format has been found or the file has ended
    }

    private void formatOne() throws IOException { // evaluation for a file that fits the first format

        CSVReader reader = new CSVReader(new FileReader("upload-dir/" + path), ',' , '"' , 0);

        String[] nextLine;

        for (int i = 0; i <= 10; i++) { // initializes all statistic objects
            statistic[i] = new Statistic();
            // 0: all (Crew + Passengers); 1-3: classes; 4-7: age groups (twenty years); 8: crew; 9+10: female+male (not used in this format)
        }

        while ((nextLine = reader.readNext()) != null) {
            if (readableLineFormatOne(nextLine)) {

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

    private void formatTwo() throws IOException { // evaluation for a file that fits the second format

        CSVReader reader = new CSVReader(new FileReader("upload-dir/" + path), ',' , '"' , 0);

        //Read CSV line by line and use the string array as you want
        String[] nextLine;

        for (int i = 0; i <= 10; i++) {
            statistic[i] = new Statistic();
            // 0: all (Crew + Passengers); 1-3: classes; 4-7: age groups (twenty years); 8: crew; 9+10: sex
        }

        while ((nextLine = reader.readNext()) != null) {
            if (readableLineFormatTwo(nextLine)) {

                int age;
                boolean death = false;
                boolean isFemale = true;

                // Age
                age = Integer.parseInt(nextLine[4].trim()); // age is in the fifth column
                statistic[0].addAgeToTotal(age, nextLine[2]); // age added to total and passes name of passenger in case they are the oldest
                statistic[0].addPassenger(); // passenger count increased by one

                // Deaths
                if (nextLine[1].equals("0")) { // column 2 equals 0
                    statistic[0].addDeadPassenger();
                    death = true;
                }

                // Sex
                if (nextLine[3].equals("female")) {
                    statistic[0].addFemale();
                    isFemale = true;
                } else if (nextLine[3].equals("male")) {
                    statistic[0].addMale();
                    isFemale = false;
                }

                // add data to respective class (or crew)
                int classNo = Integer.parseInt(nextLine[0]);
                statistic[classNo].addAgeToTotal(age, nextLine[2]);
                statistic[classNo].addPassenger();
                if (death) {
                    statistic[classNo].addDeadPassenger();
                }
                if (isFemale) {
                    statistic[classNo].addFemale();
                } else {
                    statistic[classNo].addMale();
                }

                // add data to respective age group
                if (age < 20) {
                    statistic[4].addAgeToTotal(age, nextLine[2]);
                    statistic[4].addPassenger();
                    if (death) {
                        statistic[4].addDeadPassenger();
                    }
                    if (isFemale) {
                        statistic[4].addFemale();
                    } else {
                        statistic[4].addMale();
                    }
                } else if (age < 40) {
                    statistic[5].addAgeToTotal(age, nextLine[2]);
                    statistic[5].addPassenger();
                    if (death) {
                        statistic[5].addDeadPassenger();
                    }
                    if (isFemale) {
                        statistic[5].addFemale();
                    } else {
                        statistic[5].addMale();
                    }
                } else if (age < 60) {
                    statistic[6].addAgeToTotal(age, nextLine[2]);
                    statistic[6].addPassenger();
                    if (death) {
                        statistic[6].addDeadPassenger();
                    }
                    if (isFemale) {
                        statistic[6].addFemale();
                    } else {
                        statistic[6].addMale();
                    }
                } else if (age < 80) {
                    statistic[7].addAgeToTotal(age, nextLine[2]);
                    statistic[7].addPassenger();
                    if (death) {
                        statistic[7].addDeadPassenger();
                    }
                    if (isFemale) {
                        statistic[7].addFemale();
                    } else {
                        statistic[7].addMale();
                    }
                }

                // add data to respective gender
                if (isFemale) {
                    statistic[9].addAgeToTotal(age, nextLine[2]);
                    statistic[9].addPassenger();
                    if (death) {
                        statistic[9].addDeadPassenger();
                    }
                    statistic[9].addFemale();
                } else {
                    statistic[10].addAgeToTotal(age, nextLine[2]);
                    statistic[10].addPassenger();
                    if (death) {
                        statistic[10].addDeadPassenger();
                    }
                    statistic[10].addMale();
                }
            }
        }
    }

    public int getValue(int group, int value) {
        switch (value) {
            case 0: return (int) statistic[group].getPassengers();
            case 1: return (int) statistic[group].getFemale();
            case 2: return (int) statistic[group].getMale();
            case 3: return (int) statistic[group].getAverageAge();
            case 4: return (int) statistic[group].getOldest();
            case 5: return (int) statistic[group].getPassengersDead();
            case 6: return (int) statistic[group].getDeathPercentage();
            default: return 0;
        }
    }

    private static boolean readableLineFormatOne(String[] line) { // true if this line is formatted correctly for this reader (format one)
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

    private static boolean readableLineFormatTwo(String[] line) { // true if this line is formatted correctly for this reader (format two)
        if (line.length > 4) { // must have more than 4 columns (class, survived, name, sex, age)
            try { // this bit is hacky: checks if column 5 includes only a number (age)
                int a = Integer.parseInt(line[4].trim()); // no exception -> return true
                return true;
            } catch (NumberFormatException e) { // parseInt will throw exception if it's not a number -> return false
                return false;
            }
        } else {
            return false;
        }
    }
}