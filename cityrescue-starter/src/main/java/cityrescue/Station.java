package cityrescue;

public class Station {
    private int x;
    private int y;
    private int id;
    private String name; 

    public Station(int id, String name, int x, int y){
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int getId () {
        return id;
    }

    
}
