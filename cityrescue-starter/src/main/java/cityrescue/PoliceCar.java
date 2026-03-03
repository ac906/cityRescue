package cityrescue;

import cityrescue.enums.UnitStatus;
import cityrescue.enums.UnitType;

public class PoliceCar extends Unit {
    public PoliceCar(int id, int stationId, int x, int y) {
        this.id = id;
        this.stationId = stationId;
        this.x = x;
        this.y = y;
        this.type = UnitType.POLICE_CAR;
        this.status = UnitStatus.IDLE;
    }
}