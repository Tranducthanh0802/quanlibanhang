package tranthanh.dmt.appbanhang.storemanage;

public class Khuyenmai {
    int id;
    String timeStart;
    String timeEnd;
    int percent;

    public Khuyenmai(int id, String timeStart, String timeEnd, int percent) {
        this.id = id;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.percent = percent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
