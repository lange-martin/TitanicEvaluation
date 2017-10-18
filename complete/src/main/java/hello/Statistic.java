package hello;

public class Statistic {

    private double passengersDead;
    private double totalAge;
    private double passengers;
    private int oldest;
    private String oldestName;

    public Statistic() {
        this.passengersDead = 0;
        this.totalAge = 0;
        this.passengers = 0;
        this.oldest = 0;
        this.oldestName = "";
    }

    public double getPassengersDead() {
        return passengersDead;
    }

    public double getTotalAge() {
        return totalAge;
    }

    public double getPassengers() {
        return passengers;
    }

    public int getOldest() {
        return oldest;
    }

    public String getOldestName() {
        return oldestName;
    }

    public void addDeadPassenger() {
        passengersDead++;
    }

    public void addPassenger() {
        this.passengers++;
    }

    public void addAgeToTotal(int age, String name) {
        if (age > this.oldest) {
            this.oldest = age;
            this.oldestName = name;
        }
        this.totalAge += age;
    }

    public double getAverageAge() {
        return Math.round(totalAge/passengers);
    }

    public double getDeathPercentage() {
        return Math.round(passengersDead*100/passengers);
    }

}
