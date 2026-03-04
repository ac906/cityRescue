package cityrescue;

import cityrescue.enums.IncidentStatus;
import cityrescue.enums.IncidentType;

public class Incident {
    private int x;
    private int y;
    private IncidentType type;
    private int severity;
    private int id;
    private IncidentStatus status;
// i realised in the video i forgot to mention constructors inside the class files
    // which are just here and allow objects to be made in main methods 
public Incident (int x, int y, IncidentType type, int id, int severity, IncidentStatus status){
    this.x = x;
    this.y = y;
    this.type = type;
    this.id = id;
    this.severity = severity;
    this.status = status;
} 

public int getIncidentId(){
    return id;
}

public IncidentStatus getIncidentStatus(){
    return status;
}

public void setStatus(IncidentStatus status){
    this.status = status;
}

public void setSeverity(int severity){
    this.severity = severity;
}

public IncidentType getIncidentType(){
    return type;
}

public int getSeverity(){
    return severity;
}

public int getX(){
    return x;
}

public int getY(){
    return y;
}
    
}