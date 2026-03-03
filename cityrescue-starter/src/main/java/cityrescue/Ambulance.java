package cityrescue;

import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

public class Ambulance extends Unit {
    public Ambulance(int id, int stationId, int x, int y) {
        this.id = id;
        this.stationId = stationId;
        this.x = x;
        this.y = y;
        this.type = UnitType.Ambulance;
        this.status = UnitStatus.IDLE;
    }
}
