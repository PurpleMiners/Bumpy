package clover.hamar_bumpy;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.Stack;


public class MyBackHurts implements SensorEventListener {
    private SensorManager sManager;
    public  Sensor userSensor;
    private Context activityContext;
    private int foundMeanUPTO;
    private float[] values;
    private float curr, oldSum,x;
    public float zeroRMS,mean;
    public Stack<Float> rmsStack;
    public boolean isListenerRegistered;
    MyBackHurts(Context context){
        activityContext=context;
        sManager= (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        userSensor=sManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sManager.registerListener(this,userSensor,SensorManager.SENSOR_DELAY_UI);
        rmsStack= new Stack<Float>();
        zeroRMS =0;
        oldSum =0;
        mean=0;x=0;
        rmsStack.push(zeroRMS);
        isListenerRegistered=true;
        foundMeanUPTO=1;
    }


    void pushZero()
    {
        rmsStack.clear();
        rmsStack.push(zeroRMS);
        foundMeanUPTO=1;
    }

    private void setRMSVal(float[] v)
    {
        if(Amar_Map.geo_location.getUserSpeed()>3) {
            curr = (float) Math.sqrt((((v[0] * v[0]) + (v[1] * v[1]) + (v[2] * v[2])) / 3));
            rmsStack.push(curr);
            findMeanRMS();
            x = findSDpart();
            if (x > 1.6 & rmsStack.size() > 10) {   //Well BUMP detected
                RoadTest.dangerIndex(x);
            }
        }
    }

    // Mean O' Mean
    private void findMeanRMS() {
        int x;
        float oldSumm=0;
        for(x=0;x<rmsStack.size();x++)
        {
            oldSumm+=rmsStack.get(x);
        }
        mean= oldSumm /(rmsStack.size());
    }


    // SD O' SD
    public float getFinalSD(float m)
    {
        int count=0;
        float sd=0;
        while (count<rmsStack.size())
        {
            sd+= Math.pow((rmsStack.get(count)-m),2);
            count++;
        }
        sd= (float) Math.sqrt(sd/(count-1));
        return sd;
    }

    // SD O' Mean
    private float findSDpart(){
        return (float) Math.pow((rmsStack.peek()-mean),(float)2);
    }


    public void registerAccel(){
        sManager.registerListener(this,userSensor,SensorManager.SENSOR_DELAY_UI);
        isListenerRegistered=true;
    }

    public void unregisterAccel(){
        sManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
            values= event.values;
            setRMSVal(values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //No need to do anything
    }
}
