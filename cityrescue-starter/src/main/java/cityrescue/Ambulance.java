package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

public class Ambulance extends Unit {
    public Ambulance(int id, int stationId, int x, int y) {
        super(id, stationId, x, y, UnitType.AMBULANCE, UnitStatus.IDLE);
    }
    public boolean incidentsResolve(IncidentType type) {
    return type == IncidentType.MEDICAL;
    }
    public int getTicksUntilDone() {
    return 2;
}
}
