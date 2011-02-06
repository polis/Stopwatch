package com.example.stopwatch;

import java.util.ArrayList;

//import com.example.stopwatch.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Stopwatch extends Activity {

	/** Called when the activity is first created. */
//	Button start_button;
//	Button point_button;

    TextView timeText;
    long startTime;
	long currentTime = 0;
	String timeString = "00:00:00";

	Button button;
	Toast msg;
	Handler timeHandler;
	
	ListView lablesView;
//	String[] timeLables;// = {"1. 00:00:00"};
//	ListAdapter listAdapter;
	ArrayList<String> timeLables = new ArrayList<String>();
	
	TimeAdapter	timeAdapter;

	
    	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Watch for button clicks.
        button = (Button) findViewById(R.id.start_button);
        button.setOnClickListener(mStartListener);

        button = (Button) findViewById(R.id.stop_button);
        button.setOnClickListener(mStopListener);

        button = (Button) findViewById(R.id.reset_button);
        button.setOnClickListener(mResetListener);

        button = (Button) findViewById(R.id.point_button);
        button.setOnClickListener(mPointListener);
                
        timeText = (TextView) findViewById(R.id.chrono);
        
        lablesView = (ListView)findViewById(R.id.list);
        
//        timeLables.add("lkjlkj");
        
        timeAdapter = new TimeAdapter(this, R.id.list, timeLables);

        lablesView.setAdapter(timeAdapter);

        timeHandler = new Handler();


    }

    private Runnable mUpdateTimeTask = new Runnable() {
    	   public void run() {
    	       	
    		   	long time = currentTime + System.currentTimeMillis() - startTime;
    		   	String displayTime = "";    		   	
    	       	int seconds = (int) (time / 1000);
    	       	int minutes = seconds / 60;
    	       	int millis =(int)(time % 1000) / 10;
    	       	seconds     = seconds % 60;
           	   	Log.d(ALARM_SERVICE, "Time");
           		
           		
           		if (minutes < 10) {
           			displayTime = displayTime + "0" + minutes;
           		} else {
           			displayTime = displayTime + minutes;
           		}

           		if (seconds < 10) {
           			displayTime = displayTime + ":0" + seconds;
           		} else {
           			displayTime = displayTime + ":" + seconds;
           		}

           		if (millis < 10) {
           			displayTime = displayTime + ":0" + millis;
           		} else {
           			displayTime = displayTime + ":" + millis;
           		}

           		timeText.setText(displayTime);
           		timeString = displayTime;

           		timeHandler.postDelayed(mUpdateTimeTask, 50);
    	   }
    	};
    
    View.OnClickListener mStartListener = new OnClickListener() {
        public void onClick(View v) {
            startTime = System.currentTimeMillis();
            timeHandler.postDelayed(mUpdateTimeTask,100);
       	
            button = (Button) findViewById(R.id.start_button);
            button.setVisibility(4);
            button = (Button) findViewById(R.id.stop_button);
            button.setVisibility(1);                       
            button = (Button) findViewById(R.id.point_button);
            button.setVisibility(1); 
            button.setEnabled(true);
            button = (Button) findViewById(R.id.reset_button);
            button.setVisibility(4); 
        }
    };
    View.OnClickListener mStopListener = new OnClickListener() {
        public void onClick(View v) {
            button = (Button) findViewById(R.id.start_button);
            button.setVisibility(1);
            button = (Button) findViewById(R.id.stop_button);
            button.setVisibility(4); 
            button = (Button) findViewById(R.id.point_button);
            button.setVisibility(4); 
            button = (Button) findViewById(R.id.reset_button);
            button.setVisibility(1); 

            currentTime = currentTime + System.currentTimeMillis() - startTime;
            timeHandler.removeCallbacks(mUpdateTimeTask);
            
        }
    };

    View.OnClickListener mPointListener = new OnClickListener() {
        public void onClick(View v) {
        	Log.d(ALARM_SERVICE, "Point taken");
           	timeLables.add( (int) (timeLables.size()+1) + ".   " + timeString);
   			timeAdapter.notifyDataSetChanged();
        }
    };

    View.OnClickListener mResetListener = new OnClickListener() {
        public void onClick(View v) {
            button = (Button) findViewById(R.id.point_button);
            button.setVisibility(1); 
            button.setEnabled(false);
            button = (Button) findViewById(R.id.reset_button);
            button.setVisibility(4); 
            timeHandler.removeCallbacks(mUpdateTimeTask);
            timeText.setText("00:00:00");
            currentTime = 0;
        	timeLables.removeAll(timeLables);
   			timeAdapter.notifyDataSetChanged();      		

        }
    };
   
    public class TimeAdapter extends ArrayAdapter<String> {
    	private LayoutInflater mInflater;
    	
    	public TimeAdapter(Context context, int resource, ArrayList<String> list) {
    		super(context, resource, list);
    		/*Getting inflater from the received context*/ 
    		mInflater = LayoutInflater.from(context);
    	}

    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		View v = convertView;
    		
    		if (v == null) {
    			v = mInflater.inflate(R.layout.list, null);
    		}
    		
    		/* Take an instance of your Object from taskList */
    		String row = timeLables.get(position);
    		
    		/* Setup views from your layout using data in Object*/
    		if (row != null) {
    			TextView timeLabel = (TextView) v.findViewById(R.id.time_label_text_view);
   				timeLabel.setText(row);
    		}
    		
    		return v;
    	}
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
	
}


