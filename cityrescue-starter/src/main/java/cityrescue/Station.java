package cityrescue;

public class Station {
    private int x;
    private int y;
    private int id;
    private String name; 
    private int capacity;

    public Station(int id, String name, int x, int y){
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.capacity = 0; // a default of 0 units 
    }

    public int getId () {
        return id;
    }

    public void setCapacity(int capacity){
        this.capacity = capacity;
    }

    public int getCapacity(){
        return capacity;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    
}

