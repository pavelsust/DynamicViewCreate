package com.example.android.dynamicviewcreate;

import android.content.Context;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AnotherTest extends AppCompatActivity {

//    R stands RadioGroup
//    S stands Spinner
//    C stands Checkbox
//    T stands TextView

    //    String viewTypes[] = {"R", "S", "C", "T"};
    List<View> allViewInstance = new ArrayList<View>();
    JSONObject jsonObject = new JSONObject();
    private JSONObject optionsObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_test);

        LinearLayout viewProductLayout = (LinearLayout) findViewById(R.id.customOptionLL);

        try {
            jsonObject =  new JSONObject(readJSON("sample.json", this));


            JSONArray customOptnList = jsonObject.getJSONArray("product_options");

            for (int noOfCustomOpt = 0; noOfCustomOpt < customOptnList.length(); noOfCustomOpt++) {

                JSONObject eachData = customOptnList.getJSONObject(noOfCustomOpt);
                TextView customOptionsName = new TextView(AnotherTest.this);
                customOptionsName.setTextSize(18);
                customOptionsName.setPadding(0, 15, 0, 15);
                customOptionsName.setText(eachData.getString("option_name"));
                viewProductLayout.addView(customOptionsName);

                if (eachData.getString("option_type").equals("S")) {

                    final JSONArray dropDownJSONOpt = eachData.getJSONArray("variants");


                    ArrayList<String> SpinnerOptions = new ArrayList<String>();


                    for (int j = 0; j < dropDownJSONOpt.length(); j++) {
                        String optionString = dropDownJSONOpt.getJSONObject(j).getString("variant_name");
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
                                String variant_name = dropDownJSONOpt.getJSONObject(position).getString("variant_name");
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



                if (eachData.getString("option_type").equals("R")) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    params.topMargin = 3;
                    params.bottomMargin = 3;

                    final JSONArray radioButtonJSONOpt = eachData.getJSONArray("variants");
                    RadioGroup rg = new RadioGroup(AnotherTest.this); //create the RadioGroup
                    allViewInstance.add(rg);
                    for (int j = 0; j < radioButtonJSONOpt.length(); j++) {

                        RadioButton rb = new RadioButton(AnotherTest.this);
                        rg.addView(rb, params);
                        if (j == 0)
                            rb.setChecked(true);
                        rb.setLayoutParams(params);
                        rb.setTag(radioButtonJSONOpt.getJSONObject(j).getString("variant_name"));
                        rb.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        String optionString = radioButtonJSONOpt.getJSONObject(j).getString("variant_name");
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


                if (eachData.getString("option_type").equals("C")) {


                    JSONArray checkBoxJSONOpt = eachData.getJSONArray("variants");

                    for (int j = 0; j < checkBoxJSONOpt.length(); j++) {

                        if (!(checkBoxJSONOpt.getJSONObject(j).getString("variant_name").equalsIgnoreCase("NO"))) {
                            CheckBox chk = new CheckBox(AnotherTest.this);
                            chk.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            allViewInstance.add(chk);
                            chk.setTag(checkBoxJSONOpt.getJSONObject(j).getString("variant_name"));
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.topMargin = 3;
                            params.bottomMargin = 3;
                            String optionString = checkBoxJSONOpt.getJSONObject(j).getString("variant_name");
                            chk.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    String variant_name = v.getTag().toString();
                                    Toast.makeText(getApplicationContext(), variant_name + "", Toast.LENGTH_LONG).show();
                                }
                            });
                            chk.setText(optionString);
                            viewProductLayout.addView(chk, params);
                        }
                    }
                }
                if (eachData.getString("option_type").equals("T")) {
                    TextInputLayout til = new TextInputLayout(AnotherTest.this);
                    til.setHint(getString(R.string.hint));
                    EditText et = new EditText(AnotherTest.this);
                    til.addView(et);
                    allViewInstance.add(et);
                    viewProductLayout.addView(til);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDataFromDynamicViews(View v) {
        try {
            JSONArray customOptnList = jsonObject.getJSONArray("product_options");
            optionsObj = new JSONObject();
            for (int noOfViews = 0; noOfViews < customOptnList.length(); noOfViews++) {
                JSONObject eachData = customOptnList.getJSONObject(noOfViews);

                if (eachData.getString("option_type").equals("S")) {
                    Spinner spinner = (Spinner) allViewInstance.get(noOfViews);

                    JSONArray dropDownJSONOpt = eachData.getJSONArray("variants");
                    String variant_name = dropDownJSONOpt.getJSONObject(spinner.getSelectedItemPosition()).getString("variant_name");
                    Log.d("variant_name", variant_name + "");
                    optionsObj.put(eachData.getString("option_name"),
                            "" + variant_name);
                }



                if (eachData.getString("option_type").equals("R")) {
                    RadioGroup radioGroup = (RadioGroup) allViewInstance.get(noOfViews);
                    RadioButton selectedRadioBtn = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                    Log.d("variant_name", selectedRadioBtn.getTag().toString() + "");
                    optionsObj.put(eachData.getString("option_name"),
                            "" + selectedRadioBtn.getTag().toString());
                }




                if (eachData.getString("option_type").equals("C")) {
                    CheckBox tempChkBox = (CheckBox) allViewInstance.get(noOfViews);
                    if (tempChkBox.isChecked()) {
                        optionsObj.put(eachData.getString("option_name"), tempChkBox.getTag().toString());
                    }
                    Log.d("variant_name", tempChkBox.getTag().toString() + "");
                }




                if (eachData.getString("option_type").equals("T")) {
                    TextView textView = (TextView) allViewInstance.get(noOfViews);
                    if (!textView.getText().toString().equalsIgnoreCase(""))
                        optionsObj.put(eachData.getString("option_name"), textView.getText().toString());
                    else
                        optionsObj.put(eachData.getString("option_name"), textView.getText().toString());
                    Log.d("variant_name", textView.getText().toString() + "");
                }


            }

            String outputData = (optionsObj + "").replace(",", "\n");
            outputData = outputData.replaceAll("[{}]","");
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


    private String readJSON(String fileName, Context context) {
        StringBuilder returnString = new StringBuilder();
        InputStream fIn = null;
        InputStreamReader isr = null;
        BufferedReader input = null;
        try {
            fIn = context.getResources().getAssets().open(fileName);
            isr = new InputStreamReader(fIn);
            input = new BufferedReader(isr);
            String line;
            while ((line = input.readLine()) != null) {
                returnString.append(line);
            }
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                if (isr != null) isr.close();
                if (fIn != null) fIn.close();
                if (input != null) input.close();
            } catch (Exception e2) {
                e2.getMessage();
            }
        }
        return returnString.toString();
    }

}