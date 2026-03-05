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
    /** Initialise takes an inputted width and height and 
     * ensures they are greater than 0, initialises the grid by
     * calling the Citymap constructor, then also set the begining
     * state for tick, counts, arrays with right space and IDs 
     */
    public void initialise(int width, int height) throws InvalidGridException {
        // gives an error when invalid grid size (as too small to create initial grid)
        if (width <= 0 || height <= 0){
        throw new InvalidGridException("Width and Height must be greater than 0");
    }   
        
        // create a new cityMap with all blockedCells set to 'false'
        cityMap = new CityMap(width,height);  

        // set the tick = 0 
        tick = 0;
        
        //set counters for all = 0
        stationCounter = 0;
        unitCounter = 0;
        incidentCounter = 0;

        // create the arrays with the right amount of space 
        stations = new Station[MAX_STATIONS];
        units = new Unit[MAX_UNITS];
        incidents = new Incident[MAX_INCIDENTS];

        //reset the nextIDs
        nextIncidentId = 1;
        nextStationId =1;
        nextUnitId =1;

    }

    @Override
    /** Uses the object created in Initialise method 
     * and then uses getter methods to return the width and 
     * hieght and put them in list form
     */
    public int[] getGridSize() {
        return new int[] {cityMap.getWidth(), cityMap.getHeight()};

    }

    @Override
    /**This method checks if the coodinates are valid, using the 
     * information stored in the citymap class, and then blocks 
     * coordinates if they are blockable
      */
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
    /**This method checks that the coordinates are valid, 
     * checking through the method of valid coordinates and then 
     * setting that tile to blocked through setBlocked method
     */
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
    /**This method adds a station checking for if the tile is blocked 
     * or if the coordinates are not valid, through calling methods in
     * the cityMap class, and then checking the name is not null
     * if these are passed the constructor to create a station is called 
     * and added to that position in the station list, and retunrs 
     * the next stationID
     */
    public int addStation(String name, int x, int y) throws InvalidNameException, InvalidLocationException {
        if (! cityMap.validCoords(x, y) || cityMap.isBlocked(x, y)){
        throw new InvalidLocationException("Tile is out of range or blocked");
    }   if (name == null) {  // covers the case when nothing inputted 
        throw new InvalidNameException("Enter a valid name");     
    }   
        stations[stationCounter]= new Station(nextStationId, name, x,y);
        stationCounter++;
        return nextStationId++; 
    }

    @Override
    /**This method removes a station, first checks it exists by
     * looping through all stations and checking ID. Then check 
     * it has no units otherwise can't be removed. If neither 
     * exception is triggered, then remove the station
     */
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
    /**This method sets the capacity of a station, first chcecking it 
     * exists, then finding the number of units at that station and 
     * using the method setCapacity given that the station contains 
     * less units than what you try to set as max
     */
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
    /** This method creates an array that is the size of the number 
     * of stations, then loop through all stations and add them to 
     * the array, they are naturally in accesdning id order
      */
    public int[] getStationIds() {
        // need an array of the size of station count 
        int[] stationIds = new int[stationCounter];
        for (int i=0; i < stationCounter; i++){
            stationIds[i]=stations[i].getId();
        } 
        // need a way to sort the array in ascending order 
        return stationIds;
    }

    @Override
    /**This method adds a unit and assigns it a station ID so need to
     * check unit type and stationID exist then ensure the station is not
     * full by calling capacity method then use a switch statement, based
     * on unitype inpitted, which calls the appropriate constructors
      */
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
    /** This removes a unit, have to check the unit exists and 
     * is in a valid state (not busy dealing with an incidentw)
     * then can take unit away and alter the units array, shifting 
     * all units with a higher id down one position and don't
     * return anythijng since void method
     */
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
        unitCounter--; // take 1 away from the unit count 
        for(int k= unitPosition; k< unitCounter; k++){
            units[k] = units[k + 1]; // shifts all the units beyond, down 1 place 
        }
        units[unitCounter] = null; // set the last space to null otherwise get duplicates
    }

    @Override
    /** Method that chnages the stationID a unit is assigned to
     * checks for errors in ecistance and then in teh unit status, if these 
     * don't throw then alter station ID and make the unit location 
     * that of the station 
     */
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
        for(int j = 0; j < unitCounter;j++){ if(units[j].getStationID() == newStationId){
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
    /** This method sets a unit to be out of service, but need to 
     * cehck that the unit exists and that it has a valid status 
     * (the unit must be IDLE not doing anything)
     */
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
    /** This method just returns an array with all the Unit ids 
     * does it in asscedning order through a for loop since 
     * units already has IDs in the correct order, so just loop 
     * and cut of the end of the array which contains null values
     */
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
        for(int i =0; i < unitCounter; i++){
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
                " INCIDENT=" + units[unitPosition].getIncident() + " WORK=" + units[unitPosition].getWork();
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
            throw new IllegalStateException("The incident status means this is not valid");    
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
        // find the incidents that are reported 
        // the incident array is already in ascending order 
        for (int i=0; i < incidentCounter; i++){
            if(incidents[i].getIncidentStatus() != IncidentStatus.REPORTED){
                continue;  // skip iteration if not got reported status
            }
            // now need to find all the units which are eligeble 
            Unit bestUnit = null;
            int bestDist = 0;
            for(int j=0; j< unitCounter; j++){
                // can't have units that are null
                if(units[j] == null){ continue;}
                // ensure IDLE as needed to be eligeble
                if(units[j].getUnitStatus() != UnitStatus.IDLE){continue;}
                // now need to check that the incident can be handled by this unit
                if(!units[j].incidentsResolve(incidents[i].getIncidentType())){continue;}
                // anything that has got to here is a valid option but now 
                // get the manhattan distance 
                int dist = Math.abs(units[j].getX() - incidents[i].getX()) +
                Math.abs(units[j].getY() - incidents[i].getY());
                if(bestUnit==null){
                    bestUnit = units[j];
                    bestDist = dist;
                } 
                else if(dist < bestDist // now deal with a tie where choose lower unit ID
                    || (dist == bestDist && units[j].getUnitId() < bestUnit.getUnitId())
                    // now deal with lower station ID
                    || (dist == bestDist && units[j].getUnitId() == bestUnit.getUnitId()
                        && units[j].getStationID() < bestUnit.getStationID())) {

                bestUnit = units[j];
                bestDist = dist;
                }
            }
            if(bestUnit == null){continue;} // can't do anything in this case just don't assign
            // dispach that incident and set status to EN ROUTE
            incidents[i].setStatus(IncidentStatus.DISPATCHED);
            bestUnit.setUnitStatus(UnitStatus.EN_ROUTE);
            bestUnit.setIncident(incidents[i].getIncidentId());
        }
    }

    @Override
    public void tick() {
        tick++;
        // need to move the units EN_ROUTE
        for(int i =0; i< unitCounter;i++){
            // first get rid of the cases where unit doesnt need to be moved
            if(units[i] == null){continue;}
            boolean arrivedThisTick = false;
           // ONLY movement is restricted to EN_ROUTE
            if(units[i].getUnitStatus() == UnitStatus.EN_ROUTE){
            // need to get the incident position
            int incidentX = -10;
            int incidentY = -10;
            for(int j=0; j< incidentCounter; j++){
                if(incidents[j].getIncidentId() == units[i].getIncident()){
                    incidentX = incidents[j].getX();
                    incidentY = incidents[j].getY();
                    break;
                }
            }
            //now need to get the distance from incident
            int distNow = Math.abs(units[i].getX() - incidentX) + 
            Math.abs(units[i].getY() - incidentY);

            //boolean variable for if the unit has been moved
            boolean moved = false;
            // check that the move would be valid in terms of board and blocking
            if (units[i].getY() > 0 && !cityMap.isBlocked(units[i].getX(),units[i].getY() -1)) {
                // now get the distance to the incident
                int newDist = Math.abs(units[i].getX() - incidentX) + Math.abs((units[i].getY() - 1) - incidentY);
                if (newDist < distNow) {
                units[i].setY(units[i].getY() -1); // since closer change the Y value
                moved = true; // set moved to true 
            }
            }
            // now basically repeat for EAST, then SOUTH, then WEST
            if (!moved && units[i].getX() < cityMap.getWidth() -1 && !cityMap.isBlocked(units[i].getX()+1,units[i].getY())) {
                int newDist = Math.abs(units[i].getX()+1 - incidentX) + Math.abs((units[i].getY()) - incidentY);
                if (newDist < distNow) {
                units[i].setX(units[i].getX() +1); 
                moved = true; 
            }
            }
            if (!moved && units[i].getY() < cityMap.getHeight() -1 && !cityMap.isBlocked(units[i].getX(),units[i].getY()+1)) {
                int newDist = Math.abs(units[i].getX() - incidentX) + Math.abs(units[i].getY()+1 - incidentY);
                if (newDist < distNow) {
                units[i].setY(units[i].getY() +1); 
                moved = true; 
            }
            }
            if (!moved && units[i].getX() > 0 && !cityMap.isBlocked(units[i].getX()-1,units[i].getY())) {
                // now get the distance to the incident
                int newDist = Math.abs(units[i].getX()-1 - incidentX) + Math.abs(units[i].getY() - incidentY);
                if (newDist < distNow) {
                units[i].setX(units[i].getX() -1); // since closer change the Y value
                moved = true; // set moved to true 
            }
            }
            // now at this point if there was no legal move, we need to make a move irrespective
            // of distance and instead just order N,E,S,W
            if(!moved) {
            // do north first
            if(units[i].getY() > 0 && !cityMap.isBlocked(units[i].getX(), units[i].getY() - 1)) {
                units[i].setY(units[i].getY() - 1);
                moved = true;
            }
            // then east 
            if(!moved && units[i].getX() < cityMap.getWidth() - 1 && !cityMap.isBlocked(units[i].getX() + 1, units[i].getY())) {
                units[i].setX(units[i].getX() + 1);
                moved = true;
            }
            // then south
            if(!moved && units[i].getY() < cityMap.getHeight() - 1 && !cityMap.isBlocked(units[i].getX(), units[i].getY() + 1)) {
                units[i].setY(units[i].getY() + 1);
                moved = true;
            }
            // then west
            if(!moved && units[i].getX() > 0 && !cityMap.isBlocked(units[i].getX() - 1, units[i].getY())) {
                units[i].setX(units[i].getX() - 1);
                moved = true;
            }
            }
            // at this point if still not managed to move then 
            // now need to mark arrival of a unit if applicable
            if(units[i].getX() == incidentX && units[i].getY() == incidentY){
                if(units[i].getUnitStatus() != UnitStatus.AT_SCENE){ // if not alreadu then new 
                    units[i].setUnitStatus(UnitStatus.AT_SCENE);
                    units[i].setWorkLeft(0);
                    arrivedThisTick =true; // start counting from next tick
                    // mark incident in progress
                    for(int k = 0; k < incidentCounter; k++){
                        if(incidents[k].getIncidentId() == units[i].getIncident()){
                        incidents[k].setStatus(IncidentStatus.IN_PROGRESS);
                        break; // incident status needs to be in progress
                    }
                }
            }
        }
    }

                // covering the case of already on scene 
                if(units[i].getUnitStatus() == UnitStatus.AT_SCENE && !arrivedThisTick){
                    units[i].setWorkLeft(units[i].getWork() + 1);
                    // ticks below shouldn't ever be less than work but still f
                if(units[i].getWork() == units[i].getTicksUntilDone()){
                    units[i].setWorkLeft(-10);// marks that the incident is done 
                }
            }
        }
        // resolve incidents in ascending incidentId order
        for(int inc = 0; inc < incidentCounter; inc++){
            if(incidents[inc] == null) continue;
                 for(int u = 0; u < unitCounter; u++){
                    if(units[u] == null) continue;
                    if(units[u].getIncident() == incidents[inc].getIncidentId()
                    && units[u].getWork() == -10){

            // resolve incident
            incidents[inc].setStatus(IncidentStatus.RESOLVED);

            // free unit
            units[u].setUnitStatus(UnitStatus.IDLE);
            units[u].setIncident(-10);
            units[u].setWorkLeft(0);

            break;
        }
    }
    }
    }

    @Override
    public String getStatus() {
        // get the tick first then 
        String status = "TICK=" + tick + "\n";
        // get obstacle count as don't have thar
        int obsCount = 0;
        for(int i = 0; i<cityMap.getHeight(); i++){
            for (int j=0;j<cityMap.getWidth();j++){
                if(cityMap.isBlocked(j,i)){
                    obsCount++;
                }
            }
        }
        // now can add to the string 
        status += "STATIONS=" + stationCounter + 
          " UNITS=" + unitCounter + 
          " INCIDENTS=" + incidentCounter + 
          " OBSTACLES=" + obsCount + "\n";

        // now the incidents section 
        status += "INCIDENTS\n";
        // need to loop through all 
        for (int k =0; k< incidentCounter; k++){
            if(incidents[k] == null){continue;}
            String unitAss = "-"; // means no unit assigned
            for (int h=0; h<unitCounter; h++){
                if(units[h] !=null && units[h].getIncident() == incidents[k].getIncidentId()){
                    unitAss= "" + units[h].getUnitId();
                    break;
                }
            }
            status += "I#" + incidents[k].getIncidentId() +
                  " TYPE=" + incidents[k].getIncidentType() +
                  " SEV=" + incidents[k].getSeverity() +
                  " LOC=(" + incidents[k].getX() + "," + incidents[k].getY() + ")" +
                  " STATUS=" + incidents[k].getIncidentStatus() +
                  " UNIT=" + unitAss + "\n";

        }
        // now need to do the units part 
        status += "UNITS\n";
            for(int g = 0; g < unitCounter; g++) {
                if(units[g] == null) continue;
                String incAss = "-";
                if(units[g].getIncident() != -10) { // using -10 for no incident
                    incAss = "" + units[g].getIncident();
                }

                String workAss = "";
                    if(units[g].getUnitStatus() == UnitStatus.AT_SCENE) {
                    workAss = " WORK=" + units[g].getWork();
                    }

                status += "U#" + units[g].getUnitId() +
                  " TYPE=" + units[g].getUnitType() +
                  " HOME=" + units[g].getStationID() +
                  " LOC=(" + units[g].getX() + "," + units[g].getY() + ")" +
                  " STATUS=" + units[g].getUnitStatus() +
                  " INCIDENT=" + incAss + workAss + "\n";
    }
    return status;
}
}
