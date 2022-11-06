package ch.so.agi.sodata.stac.model;


public class BoundingBox {
    private double left = 2592550; //7.34;
    private double bottom = 1213700; // 47.074;
    private double right = 2644770; // 8.032;
    private double top = 1261340; // 47.503;
    
    public double getLeft() {
        return left;
    }
    public void setLeft(double left) {
        this.left = left;
    }
    public double getBottom() {
        return bottom;
    }
    public void setBottom(double bottom) {
        this.bottom = bottom;
    }
    public double getRight() {
        return right;
    }
    public void setRight(double right) {
        this.right = right;
    }
    public double getTop() {
        return top;
    }
    public void setTop(double top) {
        this.top = top;
    }
}
