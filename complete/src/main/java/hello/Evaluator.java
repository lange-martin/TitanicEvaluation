package hello;

import java.io.FileReader;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVReader;

public class Evaluator {

    private String path;

    private Statistic[] statistic = new Statistic[8];

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

        for (int i = 0; i < 8; i++) {
            statistic[i] = new Statistic();
        }

        while ((nextLine = reader.readNext()) != null) {
            if (isReadablePassenger(nextLine)) {

                int age;
                boolean death = false;

                // Age
                age = Integer.parseInt(nextLine[2].trim());
                statistic[0].addAgeToTotal(age, nextLine[1]+" "+nextLine[0]);
                statistic[0].addPassenger();

                // Deaths
                if (nextLine.length >= 5) {
                        statistic[0].addDeadPassenger();
                        death = true;
                }

                // add data to respective class
                if (nextLine.length >=4) {
                    String classCell = nextLine[3].trim();
                    classCell = classCell.substring(0, 1);
                    int arrayField;
                    switch (classCell) {
                        case "1": arrayField = 1;
                            break;
                        case "2": arrayField = 2;
                            break;
                        case "3": arrayField = 3;
                            break;
                        default: arrayField = 3;
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

    public String report(int group) {
        String output = "";
        output += "Passengers: " + statistic[group].getPassengers() + " | ";
        output += "Average Age: " + statistic[group].getAverageAge() + " | ";
        output += "Oldest Person: " + statistic[group].getOldestName() + ", " + statistic[group].getOldest() + " | ";
        output += "Deaths: " + statistic[group].getPassengersDead() + " | ";
        output += "Death Percentage: " + statistic[group].getDeathPercentage();
        return output;
    }

    private static boolean isReadablePassenger(String[] line) {
        if (line.length > 3) {
            try {
                int a = Integer.parseInt(line[2].trim());
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        } else {
            return false;
        }
    }
}
