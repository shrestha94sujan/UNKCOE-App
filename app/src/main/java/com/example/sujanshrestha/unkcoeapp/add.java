package com.example.sujanshrestha.unkcoeapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android. view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Created by Sujan Shrestha on 9/12/17.
 */

public class add extends Activity implements Methods{

    EditText computerName;
    EditText user;
    EditText admin_UserName;
    EditText admin_Password;
    EditText location;
    EditText computer_Type;
    EditText serial_Num;
    EditText status;
    EditText devices;

    String ComputerName;
    String User;
    String Admin_UserName;
    String Admin_Password;
    String Location;
    String Computer_Type;
    String Serial_Num;
    String Status;
    String Devices;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Opens app in portrait mode only
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.add_layout);

        computerName = (EditText) findViewById(R.id.Computer_ID);
        user = (EditText) findViewById(R.id.User);
        admin_UserName = (EditText) findViewById(R.id.Admin_UserName);
        admin_Password = (EditText) findViewById(R.id.Admin_Password);
        location = (EditText) findViewById(R.id.Location);
        computer_Type = (EditText) findViewById(R.id.Computer_Type);
        serial_Num = (EditText) findViewById(R.id.Computer_Serial_Num);
        status = (EditText) findViewById(R.id.Status);
        devices = (EditText) findViewById(R.id.Device);



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method that runs when Done button clicked
     * @param view
     */
    public void onClickDone(View view) {

        // this is the Asynctask which is used to process
        Add add = new Add();
        //run background to reduce the load on the app process
        add.execute("");

    }
    /**
     * Method that runs when Back button is clicked
     * @param view
     */
    public void onClickBack (View view)
    {
        addClickEffect(view);
        Intent intent =  new Intent(this, homePage.class);
        startActivity(intent);

    }


    @SuppressLint("NewApi")
    public void addClickEffect(View view)
    {
        Drawable drawableNormal = view.getBackground();

        Drawable drawablePressed = view.getBackground().getConstantState().newDrawable();
        drawablePressed.mutate();
        drawablePressed.setColorFilter(Color.argb(100, 0, 0, 0), PorterDuff.Mode.SRC_ATOP);

        StateListDrawable listDrawable = new StateListDrawable();
        listDrawable.addState(new int[] {android.R.attr.state_pressed}, drawablePressed);
        listDrawable.addState(new int[] {}, drawableNormal);
        view.setBackground(listDrawable);
    }

    private  class Add extends AsyncTask<String, String, String> {
        String message = "";
        Boolean isSuccessful = false;
        String varSQL1 = "(?,?,?,?,?,?,?,?,?,?)";
        @Override
        protected void onPreExecute() {
            ComputerName = computerName.getText().toString();
            User = user.getText().toString();
            Admin_UserName = admin_UserName.getText().toString();
            Admin_Password = admin_Password.getText().toString();
            Location = location.getText().toString();
            Computer_Type = computer_Type.getText().toString();
            Serial_Num = serial_Num.getText().toString();
            Status = status.getText().toString();
            Devices = devices.getText().toString();
        }
        @Override
        protected String doInBackground(String... strings) {
            String call;
            call = "{CALL UNKCOEInventory.AddRecord" + varSQL1 + "}";
            CallableStatement stmt = null;

            try {
                stmt = MainActivity.conn.prepareCall(call);
                stmt.setString(1, Serial_Num);
                stmt.setString(2, ComputerName);
                stmt.setString(3, User);
                stmt.setString(4, Admin_UserName);
                stmt.setString(5, Admin_Password);
                stmt.setString(6, Location);
                stmt.setString(7, Computer_Type);
                stmt.setString(8, Status);
                stmt.setString(9, Devices);
                stmt.registerOutParameter(10, Types.VARCHAR);
                stmt.executeQuery();
                isSuccessful = true;
                message = stmt.getString(10);
                stmt.close();
            } catch (SQLException e) {
                Log.e("Connection",e.getMessage());
            }
            return message;
        }
        @Override
        protected void onPostExecute(String s) {
            if (isSuccessful) {
                Toast.makeText(add.this, s, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(add.this, add.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
