package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

public abstract class Unit {
    private int id; 
    private int stationId;
    private int x;
    private int y;
    private UnitType type;
    private UnitStatus status;
    private int incidentId;
    private int workLeft; 
    private int ticksUntilDone;

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

    public abstract boolean incidentsResolve(IncidentType type);

    public void setTicksUntilDone(int ticksUntilDone){
        this.ticksUntilDone = ticksUntilDone;
    }

    public int getTicksUntilDone(){
        return ticksUntilDone;
    }

}

