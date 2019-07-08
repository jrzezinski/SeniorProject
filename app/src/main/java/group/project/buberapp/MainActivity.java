// By Justin Rzezinski

package group.project.buberapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MainActivity";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_NAME = "Name";
    private static final String KEY_PHONE = "Phone";
    private static final String KEY_BOAT = "Boat";
    private static final String KEY_DLNUMBER = "DL Number";
    private static final String KEY_BOATINGID = "Boating ID";

    // Fields from app
    private TextInputLayout textEmail;
    private TextInputLayout textPassword;
    private TextInputLayout textName;
    private TextInputLayout textPhone;
    private TextInputLayout textDriverId;
    private TextInputLayout textBoatId;
    private Spinner typeSelect;
    private Switch signupSwitch;
    private Switch captainSwitch;

    private EditText dataEmail;
    private EditText dataPassword;
    private EditText dataName;
    private EditText dataPhone;
    private EditText dataBoatID;
    private EditText dataDLNumber;

    // Greeter fields from xml form
    private TextView capSignup;
    private TextView riderSignup;
    private TextView capLogin;
    private TextView riderLogin;

    //Cloud Firestore Instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference capRef = db.collection("captain");
    private CollectionReference userRef = db.collection("users");

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
        textDriverId = findViewById(R.id.text_input_driverId);
        textBoatId = findViewById(R.id.text_input_boatId);
        typeSelect = findViewById(R.id.type_spinner);
        signupSwitch = findViewById(R.id.signup_switch);
        captainSwitch = findViewById(R.id.captain_switch);

        dataEmail = findViewById(R.id.email);
        dataPassword = findViewById(R.id.password);
        dataName = findViewById(R.id.name);
        dataPhone = findViewById(R.id.phone);
        dataDLNumber = findViewById(R.id.driverId);
        dataBoatID = findViewById(R.id.boatId);

        // Initialize greeter fields from xml form
        capSignup = findViewById(R.id.greet_captain_signup);
        riderSignup = findViewById(R.id.greet_rider_signup);
        capLogin = findViewById(R.id.greet_captain_login);
        riderLogin = findViewById(R.id.greet_rider_login);

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
        textBoatId.setVisibility(View.GONE);
        textDriverId.setVisibility(View.GONE);

        // Listen for Switch changes
        signupSwitch.setOnCheckedChangeListener(this);
        captainSwitch.setOnCheckedChangeListener(this);

        // show appropriate greeting
        capSignup.setVisibility(View.GONE);
        riderSignup.setVisibility(View.GONE);
        capLogin.setVisibility(View.GONE);
        riderLogin.setVisibility(View.VISIBLE);
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

    private boolean checkDriver()
    {
        String driverIn = textDriverId.getEditText().getText().toString().trim();

        if (driverIn.isEmpty())
        {
            textDriverId.setError("Please use a valid Liscence Number.");
            return false;
        }
        else
        {
            textDriverId.setError(null);
            return true;
        }
    }

    private boolean checkBoatSafety()
    {
        String boatIn = textBoatId.getEditText().getText().toString().trim();

        if (boatIn.isEmpty())
        {
            textBoatId.setError("Please use a valid email.");
            return false;
        }
        else
        {
            textBoatId.setError(null);
            return true;
        }
    }

    // Check app input, if good go to next page
    public void confirmInput(View v)
    {
        String emailIn = dataEmail.getText().toString();
        String passIn = dataPassword.getText().toString();
        String nameIn = dataName.getText().toString();
        String phoneIn = dataPhone.getText().toString();
        String driverIn = dataDLNumber.getText().toString();
        String boatIn = dataBoatID.getText().toString();

        final Map<String,Object> myCap = new HashMap<String,Object>();
        myCap.put(KEY_EMAIL, emailIn);
        myCap.put(KEY_PASSWORD, passIn);
        myCap.put(KEY_NAME, nameIn);
        myCap.put(KEY_PHONE, phoneIn);
        myCap.put(KEY_DLNUMBER, driverIn);
        myCap.put(KEY_BOATINGID , boatIn);

        final Map<String,Object> myUser = new HashMap<String,Object>();
        myUser.put(KEY_EMAIL, emailIn);
        myUser.put(KEY_PASSWORD, passIn);
        myUser.put(KEY_NAME, nameIn);
        myUser.put(KEY_PHONE, phoneIn);

        if(signupSwitch.isChecked() && captainSwitch.isChecked())
        {
            // Captain Signup field checks
            if(!checkEmail() | !checkPass() | !checkName() | !checkPhone() | !checkDriver() | !checkBoatSafety())
            {
                return;
            }

            // Use this block to check if field exists and add user if not
            final String currentEmail = textEmail.getEditText().getText().toString().trim();
            Query query = capRef.whereEqualTo("Email", currentEmail);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(MainActivity.this, "Email found! Please sign in instead!", Toast.LENGTH_SHORT).show();
                    }

                    if(task.getResult().size() == 0)
                    {
                        // add captain to DB
                        capRef.add(myCap).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(MainActivity.this, "User Saved", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, e.toString());
                            }
                        });
                    }
                }
            });
        }
        else if(signupSwitch.isChecked())
        {
            // User Signup field check
            if(!checkEmail() | !checkPass() | !checkName() | !checkPhone())
            {
                return;
            }

            // Use this block to check if field exists and add user if not
            final String currentEmail = textEmail.getEditText().getText().toString().trim();
            Query query = capRef.whereEqualTo("Email", currentEmail);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task)
                  {
                      if (task.isSuccessful())
                      {
                          Toast.makeText(MainActivity.this, "Email found! Please sign in instead!", Toast.LENGTH_SHORT).show();
                      }

                      if(task.getResult().size() == 0)
                      {
                          // add user to DB
                          userRef.add(myUser).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                              @Override
                              public void onSuccess(DocumentReference documentReference) {
                                  Toast.makeText(MainActivity.this, "User Saved", Toast.LENGTH_SHORT).show();
                                  startActivity(new Intent(MainActivity.this, MainActivity.class));
                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                                  Log.d(TAG, e.toString());
                              }
                          });
                      }
                  }
            });
        }
        else if(captainSwitch.isChecked())
        {
            // Captain Signup field check
            if(!checkEmail() | !checkPass())
            {
                return;
            }

            // Use this block to check if field exists
            final String currentEmail = textEmail.getEditText().getText().toString().trim();
            final String currentPass = textPassword.getEditText().getText().toString().trim();
            Query query = capRef.whereEqualTo("Email", currentEmail);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for(DocumentSnapshot snap : task.getResult())
                        {
                            String email = snap.getString("Email");
                            String pass = snap.getString("Password");
                            String name = snap.getString("Name");
                            String phone = snap.getString("Phone");
                            String boatId = snap.getString("Boating ID");
                            String userDL = snap.getString("DL Number");
                            String userId = snap.getId();

                            if(email.equals(currentEmail) && pass.equals(currentPass))
                            {
                                Toast.makeText(MainActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();

                                // Open second page/activity (UserHome)
                                Intent intent = new Intent(MainActivity.this, UserHome.class);
                                intent.putExtra("EXTRA_Final_email", email);
                                intent.putExtra("EXTRA_Final_pass", pass);
                                intent.putExtra("EXTRA_Final_name", name);
                                intent.putExtra("EXTRA_Final_phone", phone);
                                intent.putExtra("EXTRA_Final_userType", "captain");
                                intent.putExtra("EXTRA_Final_userId", userId);
                                intent.putExtra("EXTRA_Final_boatId", boatId);
                                intent.putExtra("EXTRA_Final_userDL", userDL);
                                startActivity(intent);
                            }
                        }
                    }

                    if(task.getResult().size() == 0)
                    {
                        Toast.makeText(MainActivity.this, "Captain not found! Please sign up!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            // User Login field check
            if(!checkEmail() | !checkPass())
            {
                return;
            }

            // Use this block to check if field exists
            final String currentEmail = textEmail.getEditText().getText().toString().trim();
            final String currentPass = textPassword.getEditText().getText().toString().trim();
            Query query = userRef.whereEqualTo("Email", currentEmail);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
            {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task)
                {
                    if(task.isSuccessful())
                    {
                        for(DocumentSnapshot snap : task.getResult())
                        {
                            String email = snap.getString("Email");
                            String pass = snap.getString("Password");
                            String name = snap.getString("Name");
                            String phone = snap.getString("Phone");
                            String userId = snap.getId();

                            if(email.equals(currentEmail) && pass.equals(currentPass))
                            {
                                Toast.makeText(MainActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();

                                // Open second page/activity (UserHome)
                                Intent intent = new Intent(MainActivity.this, UserHome.class);
                                intent.putExtra("EXTRA_Final_email", email);
                                intent.putExtra("EXTRA_Final_pass", pass);
                                intent.putExtra("EXTRA_Final_name", name);
                                intent.putExtra("EXTRA_Final_phone", phone);
                                intent.putExtra("EXTRA_Final_userType", "user");
                                intent.putExtra("EXTRA_Final_userId", userId);
                                startActivity(intent);
                            }
                        }
                    }

                    if(task.getResult().size() == 0)
                    {
                        Toast.makeText(MainActivity.this, "Rider not found! Please sign up!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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
            //typeSelect.setVisibility(View.VISIBLE);
            textBoatId.setVisibility(View.VISIBLE);
            textDriverId.setVisibility(View.VISIBLE);

            // show appropriate greeting
            capSignup.setVisibility(View.VISIBLE);
            riderSignup.setVisibility(View.GONE);
            capLogin.setVisibility(View.GONE);
            riderLogin.setVisibility(View.GONE);
        }
        // signup switch check, reveals/hides fields for user signup
        else if(signupSwitch.isChecked())
        {
            textName.setVisibility(View.VISIBLE);
            textPhone.setVisibility(View.VISIBLE);
            //typeSelect.setVisibility(View.GONE);
            textBoatId.setVisibility(View.GONE);
            textDriverId.setVisibility(View.GONE);

            // show appropriate greeting
            capSignup.setVisibility(View.GONE);
            riderSignup.setVisibility(View.VISIBLE);
            capLogin.setVisibility(View.GONE);
            riderLogin.setVisibility(View.GONE);

        }
        else if(captainSwitch.isChecked())
        {
            textName.setVisibility(View.GONE);
            textPhone.setVisibility(View.GONE);
            //typeSelect.setVisibility(View.GONE);
            textBoatId.setVisibility(View.GONE);
            textDriverId.setVisibility(View.GONE);

            // show appropriate greeting
            capSignup.setVisibility(View.GONE);
            riderSignup.setVisibility(View.GONE);
            capLogin.setVisibility(View.VISIBLE);
            riderLogin.setVisibility(View.GONE);

        }
        else
        {
            textName.setVisibility(View.GONE);
            textPhone.setVisibility(View.GONE);
            //typeSelect.setVisibility(View.GONE);
            textBoatId.setVisibility(View.GONE);
            textDriverId.setVisibility(View.GONE);

            // show appropriate greeting
            capSignup.setVisibility(View.GONE);
            riderSignup.setVisibility(View.GONE);
            capLogin.setVisibility(View.GONE);
            riderLogin.setVisibility(View.VISIBLE);
        }
    }
}
