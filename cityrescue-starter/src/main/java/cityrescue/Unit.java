package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

public abstract class Unit { //class has to be abstract so units must be created in subclasses 
    protected int id;  // protected allows subclasses (ambulance/fireengine/policecar) to access but maintains encapsulation
    protected int stationId;
    protected int x;
    protected int y;
    protected UnitType type;
    protected UnitStatus status;
    protected int incidentId;
    protected int workLeft; 

    public int getStationID(){
        return stationId;
    }
   

    public Unit(int id, int stationId, int x, int y, UnitType type, UnitStatus status){
        this.id = id;
        this.stationId = stationId;
        this.x = x;
        this.y = y;
        this.type = type;
        this.status = status;
    }

    public int getUnitId(){
        return id;
    }

    public UnitStatus getUnitStatus(){
        return status;
    }

    public void setStationID(int stationId){
        this.stationId = stationId;
    }

    public void setX( int x ){
        this.x = x;
    }

    public void setY( int y ){
        this.y = y;
    }

    public void setUnitStatus(UnitStatus status){
        this.status = status;
    }

    public UnitType getUnitType(){
        return type;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

    public int getIncident(){
        return incidentId;
    }

    public int getWork(){
        return workLeft;
    }

    public void setIncident(int incidentId){
        this.incidentId = incidentId;
    }

    public void setWorkLeft(int workLeft){
        this.workLeft = workLeft;
    }

}

