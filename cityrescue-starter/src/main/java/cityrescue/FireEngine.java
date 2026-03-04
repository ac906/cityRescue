package cityrescue;

import cityrescue.enums.IncidentType;
import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

public class FireEngine extends Unit {
    public FireEngine(int id, int stationId, int x, int y) {
        super(id, stationId, x, y, UnitType.FIRE_ENGINE, UnitStatus.IDLE);
    }
    public boolean incidentsResolve(IncidentType type) {
    return type == IncidentType.FIRE;
    }
    public int getTicksUntilDone() {
    return 4;
}
}