package cityrescue;

import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

public class FireEngine extends Unit {
    public FireEngine(int id, int stationId, int x, int y) {
        this.id = id;
        this.stationId = stationId;
        this.x = x;
        this.y = y;
        this.type = UnitType.FireEngine;
        this.status = UnitStatus.IDLE;
    }
}