package cityrescue;

public class CityMap {
    private int width;
    private int height;
    private boolean[][] blockedCells;
    // i realised in the video i forgot to mention constructors inside the class files
    // which are just here which allow objects to be made in methods
    public CityMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.blockedCells = new boolean[width][height];
    }

    public int getWidth (){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public boolean isBlocked(int x, int y){
        return blockedCells[x][y];
    }

    public boolean validCoords (int x, int y){
        return x >= 0 && x< width && y >= 0 && y < height;
    }

    public void  setBlocked(int x, int y, boolean value){
        blockedCells[x][y] = value;
    }
    
}
