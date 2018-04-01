package neoncore.com.servi.beans;

/**
 * Created by Musa on 3/23/2018.
 */

public class DateObject {
    int year,month,day;

    public DateObject(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public DateObject() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
