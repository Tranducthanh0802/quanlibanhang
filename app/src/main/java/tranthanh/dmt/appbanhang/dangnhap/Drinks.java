package tranthanh.dmt.appbanhang.dangnhap;

public class Drinks {
    String name;
    String category;
    int cost;
    int sl=1;

    public int getSl() {
        return sl;
    }

    public void setSl(int sl) {
        this.sl = sl;
    }

    public Drinks(String name, String category, int cost) {
        this.name = name;
        this.category = category;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
