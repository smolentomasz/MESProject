public class Node {
    private double x;
    private double y;
    private double t; // temperatura
    private boolean BC; // warunek brzegowy

    public Node() {
        this.x = 0;
        this.y = 0;
        this.t = 0;
        this.BC = false;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public boolean isBC() {
        return BC;
    }

    public void setBC(double height, double width) {
        if(x == 0 || x == width || y == 0 || y == height)
            BC = true;
        else
            BC = false;
    }
    public void setBCReact(boolean BCCondition){
        this.BC = BCCondition;
    }
}
