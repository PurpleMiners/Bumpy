package clover.hamar_bumpy;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;


public class RoadTest {
    //easeBumps | minorBumps | mediumBumps | hardBumps | toughBumps | vToughBumps;
    private static int[] dangerTags,colorCodes;
    private LatLng oldL,newL;
    private Context activityContext;
    //Links to Geo_Location and MyBackHurts
    //Gets Data from them and calculate the bump level
    RoadTest(Context context){
        activityContext=context;
        dangerTags=new int[6];
        colorCodes=new int[6];
        colorCodes[0]=Color.parseColor("#A00C00");
        colorCodes[1]=Color.parseColor("#FF1300");
        colorCodes[2]=Color.parseColor("#ff663b");
        colorCodes[3]=Color.parseColor("#FF9469");
        colorCodes[4]=Color.parseColor("#FF6A5E");
        colorCodes[5]=Color.parseColor("#D6FFAA");
    }

    public void refreshRoadDanger()
    {
        dangerTags[0]=0;
        dangerTags[1]=0;
        dangerTags[2]=0;
        dangerTags[3]=0;
        dangerTags[4]=0;
    }


    public static void dangerIndex(float index) {
        //Hamar_maps detected something now let us create an application where we can set danger Indices
        if(index < 4.8)
            dangerTags[0]++;        //ease
        else if(index<9.6)
            dangerTags[1]++;        //minor
        else if(index<15.2)
            dangerTags[2]++;        //medium
        else if(index<22.5)
            dangerTags[3]++;        //tough
        else
            dangerTags[4]++;        //Vtough
    }


//    Polyline line = mMap.addPolyline(new PolylineOptions()
//            .add(new LatLng(cry.getLatitude(), cry.getLongitude()), new LatLng(40.7, -74.0))
//            .width(5)
////            .color(Color.RED));
//    <color name="roadHeaven">#FFD3AA</color>
//    <color name="roadEase">#FFAB5E</color>
//    <color name="roadMinor">#FF6A5E</color>
//    <color name="roadMedium">#FF9469</color>
//    <color name="roadHard">#ff663b</color>
//    <color name="roadTough">#FF1300</color>
//    <color name="roadVT">#A00C00</color>

    public void drawDanger(Location old, Location l2)
    {
        oldL=new LatLng(old.getLatitude(),old.getLongitude());
        newL=new LatLng(l2.getLatitude(),l2.getLongitude());

        if(dangerTags[4]>0)
                Amar_Map.mMap.addPolyline(new PolylineOptions().add(oldL,newL)
                        .color(colorCodes[0])
                        .width(5));
            else if(dangerTags[3]>0)
            Amar_Map.mMap.addPolyline(new PolylineOptions().add(oldL,newL)
                    .color(colorCodes[1])
                    .width(5));
               else  if(dangerTags[2]>0)
            Amar_Map.mMap.addPolyline(new PolylineOptions().add(oldL,newL)
                    .color(colorCodes[2])
                    .width(5));
                   else  if(dangerTags[1]>0)
            Amar_Map.mMap.addPolyline(new PolylineOptions().add(oldL,newL)
                    .color(colorCodes[3])
                    .width(5));
                   else if(dangerTags[0]>0)
            Amar_Map.mMap.addPolyline(new PolylineOptions().add(oldL,newL)
                    .color(colorCodes[4])
                    .width(5));
                   else
            Amar_Map.mMap.addPolyline(new PolylineOptions().add(oldL,newL)
                    .color(colorCodes[5])
                    .width(5));
    }

}

