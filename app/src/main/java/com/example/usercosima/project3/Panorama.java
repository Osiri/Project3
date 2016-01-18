package com.example.usercosima.project3;
import org.osmdroid.util.GeoPoint;

/**
 * Created by User Cosima on 13.01.2016.
 */
public class Panorama {

    public String name;
    public int NumberOfBuildings;
    public int panoramaId;
    public String ToastInfo;
    public GeoPoint point;
    public Building[] allBuildings;

    public Panorama(String name, int NumberOfBuildings, int panoramaId, double Latitude, double Longitude){
        this.name = name;
        this.NumberOfBuildings = NumberOfBuildings;
        this.panoramaId = panoramaId;
        this.point = new GeoPoint(Latitude, Longitude);
        allBuildings = new Building[this.NumberOfBuildings];
    }

    public void addBuilding(int index, double startAngle, double endAngle, String Info){

        Building house = new Building(startAngle, endAngle, Info);
        allBuildings[index]= house;
    }

    public void CheckAngle(Float touchPosition){

        for(int j = 0; j< NumberOfBuildings; j++ ){

            if(touchPosition > allBuildings[j].startAngle && touchPosition < allBuildings[j].endAngle){

                this.ToastInfo = allBuildings[j].Info;
                break;
            }
        }
    }
}
