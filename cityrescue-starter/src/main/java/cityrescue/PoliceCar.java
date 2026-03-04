package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

public class PoliceCar extends Unit {
   public PoliceCar (int id, int stationId, int x, int y) {
        super(id, stationId, x, y, UnitType.POLICE_CAR, UnitStatus.IDLE);
        // need super to run the parent constructor in the unit class, 
        // then set specific values to police car, i found on W3S schools 
        // when asking how to call parent class constructor from a child class
    }
    public boolean incidentsResolve(IncidentType type) {
    return type == IncidentType.CRIME;
    }
    public int getTicksUntilDone() {
    return 3;
}
}