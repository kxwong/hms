package Controller;

import java.util.Date;

public class DateToString {

    public DateToString() {
    }

    public String ToFormatDate(Date date) {
        return date.getDate() + " " + ToStringMonth(date.getMonth()) + " " + (date.getYear() + 1900);
    }

    public String ToStringMonth(int month) {
        String mon = "Jan";
        if (month == 1) {
            mon = "Feb";
        } else if (month == 2) {
            mon = "Mar";
        } else if (month == 3) {
            mon = "Apr";
        } else if (month == 4) {
            mon = "May";
        } else if (month == 5) {
            mon = "Jun";
        } else if (month == 6) {
            mon = "Jul";
        } else if (month == 7) {
            mon = "Aug";
        } else if (month == 8) {
            mon = "Sep";
        } else if (month == 9) {
            mon = "Oct";
        } else if (month == 10) {
            mon = "Nov";
        } else if (month == 11) {
            mon = "Dec";
        }
        return mon;
    }

    public Date StringToStartDate(String dateString) {
        Date date = new Date();
        date.setDate(Integer.parseInt(dateString.split("-")[2]));
        date.setHours(0);
        date.setMinutes(0);
        date.setMonth(Integer.parseInt(dateString.split("-")[1]) - 1);
        date.setSeconds(0);
        date.setYear(Integer.parseInt(dateString.split("-")[0]) - 1900);
        return date;
    }

    public Date StringToEndDate(String dateString) {
        Date date = StringToStartDate(dateString);
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date;
    }

    public Date DateTimeStringToDate(String dateString) {
        Date date = new Date();
        date.setDate(Integer.parseInt(dateString.split("T")[0].split("-")[2]));
        date.setHours(Integer.parseInt(dateString.split("T")[1].split(":")[0]));
        date.setMinutes(Integer.parseInt(dateString.split("T")[1].split(":")[1]));
        date.setMonth(Integer.parseInt(dateString.split("T")[0].split("-")[1]) - 1);
        date.setSeconds(0);
        date.setYear(Integer.parseInt(dateString.split("T")[0].split("-")[0]) - 1900);
        return date;
    }

    public Date DateToEndDate(Date date) {
        date.setHours(23);
        date.setMinutes(59);
        date.setSeconds(59);
        return date;
    }

    public String DateToStringTime(Date date) {
        return (date.getHours() > 12 ? String.valueOf(date.getHours() - 12) : (date.getHours() == 0 ? "12" : date.getHours())) + ":" + (date.getMinutes() == 0 ? "00" : "30") + " " + (date.getHours() >= 12 ? "PM" : "AM");
    }

    public String DateTimeToTime(Date date) {
        return (date.getHours() > 12 ? String.valueOf(date.getHours() - 12) : (date.getHours() == 0 ? "12" : date.getHours())) + ":" + (date.getMinutes() < 10 ? "0" : "") + date.getMinutes() + " " + (date.getHours() >= 12 ? "PM" : "AM");
    }

    public String DateTimeWithSecToTime(Date date) {
        return (date.getHours() > 12 ? String.valueOf(date.getHours() - 12) : (date.getHours() == 0 ? "12" : date.getHours())) + ":" + (date.getMinutes() < 10 ? "0" : "") + date.getMinutes() + ":" + (date.getSeconds()< 10 ? "0" : "") + date.getSeconds() + " " + (date.getHours() >= 12 ? "PM" : "AM");
    }

    public Date HourMinToDate(String time) {
        Date date = new Date();
        int hour = Integer.valueOf(time.split(":")[0]);
        int minutes = Integer.valueOf(time.split(":")[1]);
        date.setHours(hour);
        date.setMinutes(minutes);
        date.setSeconds(0);
        return date;
    }

    public String DateToHourMinute(Date date) {
        int hour = date.getHours();
        int minutes = date.getMinutes();
        return ((hour < 10 ? "0" + String.valueOf(hour) : String.valueOf(hour)) + ":" + (minutes < 10 ? "0" + String.valueOf(minutes) : String.valueOf(minutes)));
    }

    public boolean IsMonthYearValid(String time) {
        boolean valid = false;
        if (time.length() == 7) {
            try {
                int month = Integer.parseInt(time.split("-")[0]);
                int year = Integer.parseInt(time.split("-")[1]);
                if (month > 0 && month <= 12) {
                    valid = true;
                }
            } catch (Exception ex) {
            }
        }
        return valid;
    }

    public boolean IsYearValid(String time) {
        boolean valid = false;
        if (time.length() == 4) {
            try {
                Integer.parseInt(time);
                valid = true;
            } catch (Exception ex) {
            }
        }
        return valid;
    }

    public Date ToStartMonth(String time) {
        if (IsMonthYearValid(time)) {
            int month = Integer.parseInt(time.split("-")[0]);
            int year = Integer.parseInt(time.split("-")[1]);
            Date date = new Date();
            date.setDate(1);
            date.setHours(0);
            date.setMinutes(0);
            date.setMonth(month - 1);
            date.setSeconds(0);
            date.setYear(year - 1900);
            return date;
        }
        return null;
    }

    public Date ToEndMonth(String time) {
        if (IsMonthYearValid(time)) {
            int month = Integer.parseInt(time.split("-")[0]);
            int year = Integer.parseInt(time.split("-")[1]);
            Date date = new Date();
            date.setDate(GetLastDate(month - 1, year));
            date.setMonth(month - 1);
            date.setYear(year - 1900);
            return DateToEndDate(date);
        }
        return null;
    }

    public Date ToStartYear(String time) {
        if (IsYearValid(time)) {
            int year = Integer.parseInt(time);
            Date date = new Date();
            date.setDate(1);
            date.setHours(0);
            date.setMinutes(0);
            date.setMonth(0);
            date.setSeconds(0);
            date.setYear(year - 1900);
            return date;
        }
        return null;
    }

    public Date ToEndYear(String time) {
        if (IsYearValid(time)) {
            int year = Integer.parseInt(time);
            Date date = new Date();
            date.setDate(31);
            date.setHours(0);
            date.setMinutes(0);
            date.setMonth(11);
            date.setSeconds(0);
            date.setYear(year - 1900);
            return date;
        }
        return null;
    }

    private int GetLastDate(int month, int year) {
        int day = 31;
        if (month == 1) {
            if (year % 4 == 0) {
                day = 29;
            } else {
                day = 28;
            }
        } else if (month == 2) {
            day = 31;
        } else if (month == 3) {
            day = 30;
        } else if (month == 4) {
            day = 31;
        } else if (month == 5) {
            day = 30;
        } else if (month == 6) {
            day = 31;
        } else if (month == 7) {
            day = 31;
        } else if (month == 8) {
            day = 30;
        } else if (month == 9) {
            day = 31;
        } else if (month == 10) {
            day = 30;
        } else if (month == 11) {
            day = 31;
        }
        return day;
    }

    public boolean IsSameDate(Date date1, Date date2) {
        return date1.getDate() == date2.getDate() && date1.getMonth() == date2.getMonth() && date1.getYear() == date2.getYear();
    }
}
