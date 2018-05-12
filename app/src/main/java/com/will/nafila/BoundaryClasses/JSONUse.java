package com.will.nafila.BoundaryClasses;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.will.nafila.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by willrcneto on 29/03/18.
 */

public class JSONUse {

    Context context;
    Activity activity;

    public JSONUse(Context context){
        this.context = context;
        this.activity = (Activity) context;
    }

    private String getJSON(){
        String json = null;
        try {
            InputStream is = activity.getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void loadCountries(Spinner spinner){

        try {

            JSONArray countries = new JSONArray(getJSON());

            List<String> countries_list = new ArrayList<>();
            countries_list.add(activity.getResources().getString(R.string.select_country));

            for(int i = 0; i<countries.length();i++){
                countries_list.add(countries.getJSONObject(i).getString("name"));
            }

            adapterSpinner(spinner, countries_list,context);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void adapterSpinner(Spinner spinner, List<String> list, Context context){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, R.layout.support_simple_spinner_dropdown_item, list);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }
}
