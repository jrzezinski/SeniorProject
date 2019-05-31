// By Justin Rzezinski

package group.project.buberapp;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    // Fields from app
    private TextInputLayout textEmail;
    private TextInputLayout textPassword;
    private TextInputLayout textName;
    private TextInputLayout textPhone;
    private Spinner typeSelect;
    private Switch signupSwitch;
    private Switch captainSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // initialize the app fields
        textEmail = findViewById(R.id.text_input_email);
        textPassword = findViewById(R.id.text_input_password);
        textName = findViewById(R.id.text_input_name);
        textPhone = findViewById(R.id.text_input_phone);
        typeSelect = findViewById(R.id.type_spinner);
        signupSwitch = findViewById(R.id.signup_switch);
        captainSwitch = findViewById(R.id.captain_switch);

        // Fill in the spinner with the String Array in strings.xml
        Spinner spinner = findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.watercraftTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Set spinner to listen for change
        spinner.setOnItemSelectedListener(this);

        // hide non applicable options
        textName.setVisibility(View.GONE);
        textPhone.setVisibility(View.GONE);
        typeSelect.setVisibility(View.GONE);

        // Listen for Switch changes
        signupSwitch.setOnCheckedChangeListener(this);
        captainSwitch.setOnCheckedChangeListener(this);
    }

    // The four check functions use local validation ***Just for testing***
    private boolean checkEmail()
    {
        String emailIn = textEmail.getEditText().getText().toString().trim();

        if (emailIn.isEmpty())
        {
            textEmail.setError("Please use a valid email.");
            return false;
        }
        else
        {
            textEmail.setError(null);
            return true;
        }
    }

    private boolean checkPass()
    {
        String passIn = textPassword.getEditText().getText().toString().trim();

        if (passIn.isEmpty())
        {
            textPassword.setError("Field cannot be empty.");
            return false;
        }
        else if (passIn.length() > 30)
        {
            textPassword.setError("Password too long.");
            return false;
        }
        else
        {
            textPassword.setError(null);
            return true;
        }
    }

    private boolean checkName()
    {
        String nameIn = textName.getEditText().getText().toString().trim();

        if (nameIn.isEmpty())
        {
            textName.setError("Field cannot be empty.");
            return false;
        }
        else if (nameIn.length() > 30)
        {
            textName.setError("Name too long");
            return false;
        }
        else
        {
            textName.setError(null);
            return true;
        }
    }

    private boolean checkPhone()
    {
        String phoneIn = textPhone.getEditText().getText().toString().trim();

        if (phoneIn.isEmpty())
        {
            textPhone.setError("Please use a valid Phone number.");
            return false;
        }
        else
        {
            textPhone.setError(null);
            return true;
        }
    }

    // Check app input, if good show popup
    public void confirmInput(View v)
    {
        // leaves method if any of the tests are false
        if(!checkEmail() | !checkPass() | !checkName() | !checkPhone())
        {
            return;
        }

        /*
        // Test display text
        String display = "Email: " + textEmail.getEditText().getText().toString();
        display += "\n";
        display += "Password: " + textPassword.getEditText().getText().toString();
        display += "\n";
        display += "Name: " + textName.getEditText().getText().toString();
        display += "\n";
        display += "Phone: " + textPhone.getEditText().getText().toString();

        Toast.makeText(this, display, Toast.LENGTH_SHORT).show();
        */

        // Open second page/activity (UserHome)
        Intent intent = new Intent(this, UserHome.class);
        startActivity(intent);
    }

    // Methods for spinner
    // Stores the spinner value
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
    {
        String watercraft = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Method for switch change
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        // captain switch check, reveals/hides fields for captain signup
        if(signupSwitch.isChecked() && captainSwitch.isChecked())
        {
            textName.setVisibility(View.VISIBLE);
            textPhone.setVisibility(View.VISIBLE);
            typeSelect.setVisibility(View.VISIBLE);
        }
        // signup switch check, reveals/hides fields for user signup
        else if(signupSwitch.isChecked())
        {
            textName.setVisibility(View.VISIBLE);
            textPhone.setVisibility(View.VISIBLE);
            typeSelect.setVisibility(View.GONE);
        }
        else
        {
            textName.setVisibility(View.GONE);
            textPhone.setVisibility(View.GONE);
            typeSelect.setVisibility(View.GONE);
        }
    }
}
