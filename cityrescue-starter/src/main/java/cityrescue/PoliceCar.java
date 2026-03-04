package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

public class PoliceCar extends Unit {
   public PoliceCar (int id, int stationId, int x, int y) {
        super(id, stationId, x, y, UnitType.POLICE_CAR, UnitStatus.IDLE);
    }
    public boolean incidentsResolve(IncidentType type) {
    return type == IncidentType.CRIME;
    }
    public int getTicksUntilDone() {
    return 3;
}
}