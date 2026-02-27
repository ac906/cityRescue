package cityrescue;

import java.nio.channels.UnsupportedAddressTypeException;

import javax.naming.InvalidNameException;

import cityrescue.enums.*;
import cityrescue.exceptions.*;

/**
 * CityRescueImpl (Starter)
 *
 * Your task is to implement the full specification.
 * You may add additional classes in any package(s) you like.
 */
public class CityRescueImpl implements CityRescue {

    private CityMap cityMap;

    // counters 
    private int tick;
    private int stationCounter;
    private int unitCounter;
    private int incidentCounter;

    // max of each unit 
    private static final int MAX_STATIONS = 20;
    private static final int MAX_UNITS = 50;
    private static final int MAX_INCIDENTS = 200; 

    private Station[] stations;
    private Unit[] units;
    private Incident[] incidents;

    //need stuff to count the ID
    private int StationId = 1;
    private int nextIncidentId = 1;
    private int nextUnitId = 1;


    @Override
    public void initialise(int width, int height) throws InvalidGridException {
        // gives an error when invalid grid size (as too small to create initial grid)
        if (width <= 0 || height <= 0){
        throw new InvalidGridException("Width and Height must be greater than 0");
    }   
        
        // create a new cityMap with all blockedCells set to 'false'
        cityMap = new CityMap(width,height);  

        // set the tick = 0 
        tick = 0;
        
        //set counters = 0
        stationCounter = 0;
        unitCounter = 0;
        incidentCounter = 0;

        // Recreate empty arrays
        stations = new Station[MAX_STATIONS];
        units = new Unit[MAX_UNITS];
        incidents = new Incident[MAX_INCIDENTS];

    }

    @Override
    public int[] getGridSize() {
        if (cityMap == null){ // idk if this is right for this exception
        throw new UnsupportedOperationException("");}
        return new int[] {cityMap.getWidth(), cityMap.getHeight()};

    }

    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {

        //cover case that inputted coordinates are outside the range of board 
        // cover case that there is no obstacle to remove
        if (! cityMap.validCoords(x, y)|| cityMap.isBlocked(x, y)){
        throw new InvalidLocationException("Tile is out of range");
    }   

        // set the tile to true  
        cityMap.setBlocked(x, y, true); 

    }

    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {

        //cover case that inputted coordinates are outside the range of board 
        // cover case that there is no obstacle to remove
        if (! cityMap.validCoords(x, y) || ! cityMap.isBlocked(x, y)){
        throw new InvalidLocationException("Tile is out of range or not blocked");

        // set the tile to false 
    }   cityMap.setBlocked(x, y, false); 
    }

        

    @Override
    public void addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        if (! cityMap.validCoords(x, y) || cityMap.isBlocked(x, y)){
        throw new InvalidLocationException("Tile is out of range or blocked");
    }   if (name == null) {  // covers the case when nothing inputted 
        throw new InvalidNameException("Enter a valid name");     
    }   
        stations[stationCounter]= new Station(nextStationId, name, x,y);
        stationCounter++;
        StationId++; // returns original ID then adds 1 
    }

    @Override
    public void removeStation(int stationId) 
    throws IDNotRecognisedException, IllegalStateException{
        int position = 21;
        for (int i = 0; i < stationCounter; i++) {
            if (stations[i].getId() == stationId) {
            position = i;
            break;
        }
        // error if the station is not found 
        if (position == 21){
            throw new IDNotRecognisedException("Station doesn't exist");
        }
        // error if the station has no units 
        for (int j=0; j < unitCounter; j++){
            if(units[j].getStationID() == stationId){
                throw new IllegalStateException("station has units");
            }
        }

        // now change the station counter and remove the station 
        stationCounter--; // -1 from the station count
        for(int k= position; k< stationCounter; k++){
            stations[i] = stations[i + 1]; // set station to left equal to one to the right
            // only perfrom this for ones above the one removed (shift them left)

            stations[stationCounter] = null; // make the last one null otherwise would 
            //have a duplicate at the end 
        }
    }
        
    }

    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getStationIds() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getUnitIds() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public int[] getIncidentIds() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void dispatch() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void tick() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public String getStatus() {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
