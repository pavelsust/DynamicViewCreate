package com.example.android.dynamicviewcreate;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnotherTest extends AppCompatActivity {


    List<View> allViewInstance = new ArrayList<View>();
    JSONObject jsonObject = new JSONObject();
    private JSONObject optionsObj;
    List<String> stringList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_test);
        new getData().execute();
    }

    private void loadJSONDate(JSONObject json){
        jsonObject = json;
        LinearLayout viewProductLayout = (LinearLayout) findViewById(R.id.customOptionLL);
        try {

            JSONArray customOptnList = json.getJSONArray("data");
            for (int noOfCustomOpt = 0; noOfCustomOpt < customOptnList.length(); noOfCustomOpt++) {


                JSONObject eachData = customOptnList.getJSONObject(noOfCustomOpt);
                TextView customOptionsName = new TextView(AnotherTest.this);
                customOptionsName.setTextSize(18);
                customOptionsName.setPadding(0, 15, 0, 15);
                customOptionsName.setText(eachData.getString(Constant.OPTION_NAME));
                viewProductLayout.addView(customOptionsName);


                if (eachData.getString(Constant.TYPE).equals(Constant.SPINNER)) {
                    final JSONArray dropDownJSONOpt = eachData.getJSONArray(Constant.VALUES);
                    ArrayList<String> SpinnerOptions = new ArrayList<String>();

                    for (int j = 0; j < dropDownJSONOpt.length(); j++) {
                        String optionString = dropDownJSONOpt.getJSONObject(j).getString(Constant.NAME);
                        SpinnerOptions.add(optionString);
                    }

                    ArrayAdapter<String> spinnerArrayAdapter = null;
                    spinnerArrayAdapter = new ArrayAdapter<String>(AnotherTest.this, R.layout.spiner_row, SpinnerOptions);
                    Spinner spinner = new Spinner(AnotherTest.this);
                    allViewInstance.add(spinner);


                    spinner.setAdapter(spinnerArrayAdapter);
                    spinner.setSelection(0, false);
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            try {
                                String variant_name = dropDownJSONOpt.getJSONObject(position).getString(Constant.NAME);
                                Toast.makeText(getApplicationContext(), variant_name + "", Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }

                    });
                    viewProductLayout.addView(spinner);
                }


//                    /***************************Radio*****************************************************/


                if (eachData.getString(Constant.TYPE).equals(Constant.RADIOBUTTON)) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 3;
                    params.bottomMargin = 3;

                    final JSONArray radioButtonJSONOpt = eachData.getJSONArray(Constant.VALUES);
                    RadioGroup rg = new RadioGroup(AnotherTest.this); //create the RadioGroup
                    allViewInstance.add(rg);


                    for (int j = 0; j < radioButtonJSONOpt.length(); j++) {

                        RadioButton rb = new RadioButton(AnotherTest.this);
                        rg.addView(rb, params);
                        if (j == 0)
                            rb.setChecked(true);
                        rb.setLayoutParams(params);
                        rb.setTag(radioButtonJSONOpt.getJSONObject(j).getString(Constant.NAME));

                        rb.setBackgroundColor(Color.parseColor("#FFFFFF"));

                        String optionString = radioButtonJSONOpt.getJSONObject(j).getString(Constant.NAME);
                        rb.setText(optionString);


                        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {

                                View radioButton = group.findViewById(checkedId);
                                String variant_name = radioButton.getTag().toString();
                                Toast.makeText(getApplicationContext(), variant_name + "", Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    viewProductLayout.addView(rg, params);
                }

//                    /***********************************CheckBox ***********************************************/


                if (eachData.getString(Constant.TYPE).equals(Constant.CHECKBOX)) {
                    JSONArray checkBoxJSONOpt = eachData.getJSONArray(Constant.VALUES);
                    for (int j = 0; j < checkBoxJSONOpt.length(); j++) {

                        //if (!(checkBoxJSONOpt.getJSONObject(j).getString("variant_name").equalsIgnoreCase("NO"))) {
                            CheckBox chk = new CheckBox(AnotherTest.this);
                            chk.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            allViewInstance.add(chk);


                            chk.setTag(checkBoxJSONOpt.getJSONObject(j).getString(Constant.NAME));
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.topMargin = 3;
                            params.bottomMargin = 3;
                            String optionString = checkBoxJSONOpt.getJSONObject(j).getString(Constant.NAME);

                            chk.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    String variant_name = v.getTag().toString();
                                    stringList.add(variant_name);

                                    //Toast.makeText(getApplicationContext(), variant_name + "", Toast.LENGTH_LONG).show();
                                }
                            });


                            chk.setText(optionString);
                            viewProductLayout.addView(chk, params);
                        }
                    //}
                }

                /*
                if (eachData.getString(Constant.TYPE).equals(Constant.EDITTEXT)) {
                    TextInputLayout til = new TextInputLayout(AnotherTest.this);
                    til.setHint(getString(R.string.hint));
                    EditText et = new EditText(AnotherTest.this);
                    til.addView(et);
                    allViewInstance.add(et);
                    viewProductLayout.addView(til);
                }

                */


                if (eachData.getString(Constant.TYPE).equals(Constant.EDITTEXT)) {
                    //TextInputLayout til = new TextInputLayout(getActivity());
                    //til.setHint(""+eachData.getString(Constant.LABEL));
                    EditText et = new EditText(getApplicationContext());
                    et.setHint("" + eachData.getString(Constant.TYPE));
                    et.setPadding(20, 40, 0, 40);

                    // Initialize a new GradientDrawable instance
                    GradientDrawable gd = new GradientDrawable();
                    gd.setColor(Color.parseColor("#ffffff"));
                    gd.setCornerRadius(3);

                    gd.setStroke(2, getResources().getColor(R.color.colorAccent));
                    et.setBackground(gd);


                    //til.addView(et);
                    allViewInstance.add(et);
                    viewProductLayout.addView(et);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getDataFromDynamicViews(View v) {
        try {
            JSONArray customOptnList = jsonObject.getJSONArray("data");
            optionsObj = new JSONObject();
            for (int noOfViews = 0; noOfViews < customOptnList.length(); noOfViews++) {
                JSONObject eachData = customOptnList.getJSONObject(noOfViews);

                if (eachData.getString(Constant.TYPE).equals(Constant.SPINNER)) {
                    Spinner spinner = (Spinner) allViewInstance.get(noOfViews);
                    JSONArray dropDownJSONOpt = eachData.getJSONArray(Constant.VALUES);
                    String variant_name = dropDownJSONOpt.getJSONObject(spinner.getSelectedItemPosition()).getString(Constant.NAME);
                    Log.d(Constant.NAME, variant_name + "");
                    optionsObj.put(eachData.getString(Constant.OPTION_NAME),
                            "" + variant_name);
                }


                if (eachData.getString(Constant.TYPE).equals(Constant.RADIOBUTTON)) {
                    RadioGroup radioGroup = (RadioGroup) allViewInstance.get(noOfViews);
                    RadioButton selectedRadioBtn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    Log.d(Constant.NAME, selectedRadioBtn.getTag().toString() + "");
                    optionsObj.put(eachData.getString(Constant.OPTION_NAME),
                            "" + selectedRadioBtn.getTag().toString());
                }


                if (eachData.getString(Constant.TYPE).equals(Constant.CHECKBOX)) {
                    CheckBox tempChkBox = (CheckBox) allViewInstance.get(noOfViews);

                    //if (tempChkBox.isChecked()) {
                        //optionsObj.put(eachData.getString(Constant.OPTION_NAME), tempChkBox.getTag().toString());
                    //}

                    optionsObj.put(eachData.getString(Constant.OPTION_NAME), stringList.toString());


                    Toast.makeText(AnotherTest.this, ""+tempChkBox.getTag().toString(), Toast.LENGTH_SHORT).show();


                    Log.d(Constant.NAME, tempChkBox.getTag().toString() + "");
                }


                if (eachData.getString(Constant.TYPE).equals(Constant.EDITTEXT)) {
                    TextView textView = (TextView) allViewInstance.get(noOfViews);
                    if (!textView.getText().toString().equalsIgnoreCase(""))
                        optionsObj.put(eachData.getString(Constant.OPTION_NAME), textView.getText().toString());
                    else
                        optionsObj.put(eachData.getString(Constant.OPTION_NAME), textView.getText().toString());
                    Log.d(Constant.NAME, textView.getText().toString() + "");
                }



            }

            String outputData = (optionsObj + "").replace(",", "\n");
            outputData = outputData.replaceAll("[{}]", "");
            ((TextView) findViewById(R.id.showData)).setText(outputData);
            Log.d("optionsObj", optionsObj + "");


            hideSoftKeyboard(findViewById(R.id.layout));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideSoftKeyboard(View v) {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    public class getData extends AsyncTask<Void , JSONObject , JSONObject>{
        JSONObject jsonObj;
        @Override
        protected JSONObject doInBackground(Void... voids) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://bollywoodgaana.com/test/dynamic.json";
            String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    jsonObj = new JSONObject(jsonStr);

                } catch (final JSONException e) {
                }

            }
            return jsonObj;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            Log.d("JSON_RESPONSE" , ""+jsonObject.toString());
            loadJSONDate(jsonObject);
        }
    }
}