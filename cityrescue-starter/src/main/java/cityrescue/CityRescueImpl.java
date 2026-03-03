package cityrescue;

import java.nio.channels.UnsupportedAddressTypeException;

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
    private int nextStationId;
    private int nextIncidentId;
    private int nextUnitId;


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

        //set the next ids = 1 
        nextIncidentId =1;
        nextStationId =1;
        nextUnitId =1;
    }

    @Override
    public int[] getGridSize() {
        return new int[] {cityMap.getWidth(), cityMap.getHeight()};

    }

    @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {

        //cover case that inputted coordinates are outside the range of board 
        // cover case that there is no obstacle to remove
        if (!cityMap.validCoords(x, y)){
        throw new InvalidLocationException("Tile is out of range");
    }   
        // set the tile to true  
        cityMap.setBlocked(x, y, true); 
    }

    @Override
    public void removeObstacle(int x, int y) throws InvalidLocationException {

        //cover case that inputted coordinates are outside the range of board 
        // cover case that there is no obstacle to remove
        if (! cityMap.validCoords(x, y)){
        throw new InvalidLocationException("Tile is out of range"); 
        }  
    // set the tile to false
    cityMap.setBlocked(x, y, false); 
    }

    @Override
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        if (! cityMap.validCoords(x, y) || cityMap.isBlocked(x, y)){
        throw new InvalidLocationException("Tile is out of range or blocked");
    }   if (name == null) {  // covers the case when it's blank  
        throw new InvalidNameException("Enter a valid name");     
    }   
        stations[stationCounter]= new Station(StationId, name, x,y);
        stationCounter++;
        StationId++; // returns original ID then adds 1 
    }

    @Override
    public void removeStation(int stationId) 
    throws IDNotRecognisedException, IllegalStateException{
        int position = -10;
        for (int i = 0; i < stationCounter; i++) {
            if (stations[i].getId() == stationId) {
            position = i;
            break;
        }
        }
        // error if the station is not found 
        if (position == -10){
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
            stations[k] = stations[k + 1]; // set station to left equal to one to the right
            // only perfrom this for ones above the one removed (shift them left)

            stations[stationCounter] = null; // make the last one null otherwise would 
            //have a duplicate at the end 
        }
    }

    @Override
    public void setStationCapacity(int stationId, int maxUnits) throws IDNotRecognisedException, InvalidCapacityException {
        Station station = null;
        for(int i=0; i< stationCounter; i++){
            if (stations[i].getId() == stationId){
                station = stations[i]; // store the name of the station as variable station
                break;
            }
        }
            if (station == null){ // case where no station with that ID is found
                throw new IDNotRecognisedException("Station doesn't exist");
            }
            int numOfUnits = 0;
            for(int k =0; k< unitCounter; k++){
               if(units[k].getStationID() == stationId){
                    numOfUnits++;
               }
            }
            if (maxUnits < numOfUnits || maxUnits<=0){
                throw new InvalidCapacityException("input for maximum units is invalid");
            }
            station.setCapacity(maxUnits);
        }


    @Override
    public int[] getStationIds() {
        // need an array of the size of station count 
        int[] stationIds = new int[stationCounter];
        for (int i=0; i < stationCounter-1; i++){
            stationIds[i]=stations[i].getId();
        } 
        // this list is natually in acsending order because stations added are always added 
        // to the end of the list and removing stations just shifts left  
        return stationIds;
    }

    @Override
    public int addUnit(int stationId, UnitType type) throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {
        // check if the unit is named 
        if (type == null){
            throw new InvalidUnitException("The unit must be named");
        }
        // check that the station can be found via its ID 
        Station station = null;
        for(int i=0; i< stationCounter; i++){
            if (stations[i].getId() == stationId){
                station = stations[i]; // store the name of the station as variable station
                break;
            }
        }
        if (station == null){
            throw new IDNotRecognisedException("Station was not found");
        }

        int numOfUnits = 0; 
        for (int k = 0; k< unitCounter; k++){
            if (units[k].getStationID() == stationId){
                numOfUnits++;
            }
        }
        if (numOfUnits>= station.getCapacity()|| unitCounter >= MAX_UNITS) {
            throw new IllegalStateException("Station is full");
        }
        // there are 3 subclasses since Unit is an abstract class so no units 
        // are created using it, therfore need to check for type to instantiate 
        // a new unit
        Unit newUnit; // new unit data type and variable name 
        switch(type){
            case AMBULANCE:
                newUnit = new Ambulance(nextUnitId, stationId, station.getX(), station.getY());
                break;
            case FIRE_ENGINE:
                newUnit = new FireEngine(nextUnitId, stationId, station.getX(), station.getY());
                break;
            case POLICE_CAR:
                newUnit = new PoliceCar(nextUnitId, stationId, station.getX(), station.getY());
                break;
            default:
                throw new InvalidUnitException("Type of unit inputted is not valud");
        }
        units[unitCounter] = newUnit; // adding the unit to the units list 
        unitCounter++;
        return nextUnitId++;
    }

    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {
        // check the unit exists
        int unitPosition = -10; // -ve number to prevent overlap with unitCounter
        for (int i=0; i < unitCounter; i++){
            if(units[i].getUnitId() == unitId){
                unitPosition = i;
                break;
            }
        }
        if(unitPosition == -10){
            throw new IDNotRecognisedException("Enter a valid unit ID");
        }
        if (units[unitPosition].getUnitStatus() == UnitStatus.EN_ROUTE ||
            units[unitPosition].getUnitStatus() == UnitStatus.AT_SCENE){
                throw new IllegalStateException("Unit is busy");
            }
        for(int k= unitPosition; k< unitCounter; k++){
            units[k] = units[k + 1]; // shifts all the units beyond, down 1 place 
        }
    }

    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {
        // check that unit and station exist
        int unitPosition = -10;
        for(int i = 0; i < unitCounter; i++){
            if(units[i].getUnitId() == unitId){
                unitPosition = i;
                break;
            }
        }
        int stationPosition = -10;
        for (int k=0; k<stationCounter; k++){
            if(stations[k].getId() == newStationId){
                stationPosition = k;
                break;
            }
        }
        if (unitPosition == -10|| stationPosition == -10){
            throw new IDNotRecognisedException("Unit/Station was not found");
        }
        int unitCountAtStation = 0;
        for(int j = 0;
                j < stationCounter;j++){ 
                    if(units[j].getStationID() == newStationId){
                    unitCountAtStation++;}}

        if(units[unitPosition].getUnitStatus() == UnitStatus.AT_SCENE || 
            units[unitPosition].getUnitStatus() == UnitStatus.EN_ROUTE ||
            units[unitPosition].getUnitStatus() == UnitStatus.OUT_OF_SERVICE || 
            unitCountAtStation >= stations[stationPosition].getCapacity()){
                throw new IllegalStateException("Unit is not IDLE or Station lacks capacity");}

        // now change the unit, so that it has a new station ID
        // do this using a setter
        units[unitPosition].setStationID(newStationId); 
        // get the location of the station and set the unitdestination to that
        units[unitPosition].setX(stations[stationPosition].getX());
        units[unitPosition].setY(stations[stationPosition].getY());
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) throws IDNotRecognisedException, IllegalStateException {
        int unitPosition = -10;
        for (int i=0; i< unitCounter; i++){
            if(units[i].getUnitId() == unitId){
                unitPosition = i;
                break;
            }}
        if(unitPosition == -10){
            throw new IDNotRecognisedException("Unit doesn't exist");
        }
        if(outOfService){
            if(units[unitPosition].getUnitStatus() == UnitStatus.IDLE){
                units[unitPosition].setUnitStatus(UnitStatus.OUT_OF_SERVICE);}
            else{ 
                throw new IllegalStateException("The unit must be IDLE");}}
        else{ units[unitPosition].setUnitStatus(UnitStatus.IDLE);}
        }

    @Override
    public int[] getUnitIds() {
        int unitIds[] = new int[unitCounter];
        for (int i=0; i< unitCounter; i++){
            unitIds[i] = units[i].getUnitId();
        }
        // need to sort them in accessending order too 
        return unitIds;
    }

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        // check the unit exists 
        int unitPosition = -10;
        for(int i =0; i < unitPosition; i++){
            if(units[i].getUnitId() == unitId){
                unitPosition = i;
            }}
        if(unitPosition == -10){
            throw new IDNotRecognisedException("unitID is not valid");
        }
        //now need to create the string in required format
        return "U#" + units[unitPosition].getUnitId() + " TYPE=" + units[unitPosition].getUnitType()
                + " HOME=" + units[unitPosition].getStationID() + " LOC=(" + units[unitPosition].getX() + "," 
                + units[unitPosition].getY() + ")" + " STATUS=" + units[unitPosition].getUnitStatus() +
                " INCIDENT=" + " WORK="
                // neeed to finish this method 
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) throws InvalidSeverityException, InvalidLocationException {
        // chcek if type is null
        if (type == null){
            throw new InvalidSeverityException("Type is not specified");}
        // check the severity is in bounds 1 to 5
        if (severity < 1 || severity >5){
            throw new InvalidSeverityException("Severity is not in bounds");}
        // check that the location is within the bounds of the gridsize
        int[] grid = getGridSize();
        if(grid[0] < x || x<0 || y<0 || grid[1] < y || cityMap.isBlocked(x, y)){
            throw new InvalidLocationException("location is out of bounds");
        }
        // create the incident report, index it using the incident count
        incidents[incidentCounter] = new Incident(x,y,type,nextIncidentId, severity, IncidentStatus.REPORTED);
        incidentCounter ++;
        return nextIncidentId++;
    }

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {
        // first need to check that the incident exists 
        int incidentPosition = -10;
        for(int i=0; i < incidentCounter; i++){
            if(incidents[i].getIncidentId() == incidentId){
                incidentPosition = i;
                break;
            }
        }
        if (incidentPosition == -10){
            throw new IDNotRecognisedException("The incident was not found");
        }
        if (incidents[incidentPosition].getIncidentStatus() == IncidentStatus.REPORTED){
            // in this case can just cancel the report of the incident 
            incidents[incidentPosition].setStatus(IncidentStatus.CANCELLED);
        } else if (incidents[incidentPosition].getIncidentStatus() == IncidentStatus.DISPATCHED){
            //in this case need to realease the unit and cancel the incident
            // find any units working on or assigned to the incident
            for (int k=0; k< unitCounter;k++){
                if(units[k].getIncident() == incidentId){
                    units[k].setUnitStatus(UnitStatus.IDLE); // change the status to IDLE
                    units[k].setIncident(-10); // arbritary value that isnt +ve
                    units[k].setWorkLeft(0); // there will be no work remaining
                    incidents[incidentPosition].setStatus(IncidentStatus.CANCELLED);
                    break;
                }
            }
        } else { throw new IllegalStateException("Can't cancel the indident");}
        }

    @Override
    public void escalateIncident(int incidentId, int newSeverity) throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {
        // check that the incident exists 
        int incidentPosition = -10; // assign a value that can't be obtained 
        for (int i=0; i< incidentCounter; i++){
            if(incidents[i].getIncidentId() == incidentId){
                incidentPosition = i;
                break;
            }
        } 
        if(incidentPosition == -10){
            throw new IDNotRecognisedException("The incident was not found");
        }
        if(newSeverity < 1 || newSeverity > 5){
            throw new InvalidSeverityException("The severity is not between valid parameters of 1 and 5");
        }
        if (incidents[incidentPosition].getIncidentStatus() == IncidentStatus.CANCELLED||
            incidents[incidentPosition].getIncidentStatus() == IncidentStatus.RESOLVED){
            throw new InvalidSeverityException("The incident status means this is not valid");    
        }
        incidents[incidentPosition].setSeverity(newSeverity); // change the severity to the new one 
    }

    @Override
    public int[] getIncidentIds(){
        int[] incidentIds = new int[incidentCounter];
        for(int i=0; i<incidentCounter;i++){
            incidentIds[i]= incidents[i].getIncidentId(); // naturally in asecending order
        }
        return incidentIds;
    }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        int incidentPosition = -10;
        for (int i=0; i< incidentCounter; i++){
            if(incidents[i].getIncidentId() == incidentId){
                incidentPosition = i;
                break;
            }
        }
        if(incidentPosition == -10){
            throw new IDNotRecognisedException("The Incident ID was not found");
        }
        // find the unit assigned
        int unitAssigned = -10;
        for (int k=0; k < unitCounter; k++){
            if(units[k].getIncident() == incidentId){
                unitAssigned = units[k].getUnitId();
                break;
            }
            }

        return "I#"+ incidents[incidentPosition].getIncidentId() + 
        " TYPE=" + incidents[incidentPosition].getIncidentType() +
        " SEV=" + incidents[incidentPosition].getSeverity() + 
        " LOC=(" + incidents[incidentPosition].getX() + "," + incidents[incidentPosition].getY() + ")"
        + " STATUS=" + incidents[incidentPosition].getIncidentStatus() + 
        " UNIT=" + unitAssigned;
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
