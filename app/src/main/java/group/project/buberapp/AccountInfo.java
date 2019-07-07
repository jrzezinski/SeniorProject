package group.project.buberapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View.OnClickListener;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import android.text.TextUtils;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Query;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class AccountInfo extends Fragment {

    private static final String TAG = "AccountInfo";
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_NAME = "Name";
    private static final String KEY_PHONE = "Phone";
    private static final String KEY_BOAT = "Boat";
    private static final String KEY_DLNUMBER = "DL Number";
    private static final String KEY_BOATINGID = "Boating ID";

    private EditText dataEmail;
    private EditText dataPassword;
    private EditText dataName;
    private EditText dataPhone;
    private EditText dataBoatID;
    private EditText dataDLNumber;

    //Cloud Firestore Instance
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference capRef = db.collection("captain");
    private CollectionReference userRef = db.collection("users");
    private DocumentReference capDoc = db.collection("captain").document(UserHome.userId);
    private DocumentReference userDoc = db.collection("users").document(UserHome.userId);

    @Nullable
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_account, container, false);

        dataEmail = view.findViewById(R.id.email);
        dataPassword = view.findViewById(R.id.password);
        dataName = view.findViewById(R.id.name);
        dataPhone = view.findViewById(R.id.phone);
        dataDLNumber = view.findViewById(R.id.driverId);
        dataBoatID = view.findViewById(R.id.boatId);

        String currentEmail = UserHome.userEmail;
        String currentName = UserHome.userName;
        String currentPass = UserHome.userPass;
        String currentPhone = UserHome.userPhone;
        String currentBoatId = UserHome.userboatId;
        String currentDL = UserHome.userDL;
        String currentID = UserHome.userId;

        dataEmail.setHint(currentEmail);
        dataPassword.setHint(currentPass);
        dataName.setHint(currentName);
        dataPhone.setHint(currentPhone);
        dataBoatID.setHint(currentBoatId);
        /* dataType.setHint(currentType); */
        dataDLNumber.setHint(currentDL);

        return view;

    }

    public void confirmUpdate(View v) {

        String emailIn = dataEmail.getText().toString();
        String passIn = dataPassword.getText().toString();
        String nameIn = dataName.getText().toString();
        String phoneIn = dataPhone.getText().toString();
        String driverIn = dataDLNumber.getText().toString();
        String boatIn = dataBoatID.getText().toString();

        capDoc.update(KEY_EMAIL, emailIn);
        capDoc.update(KEY_PASSWORD, passIn);
        capDoc.update(KEY_NAME, nameIn);
        capDoc.update(KEY_PHONE, phoneIn);
        capDoc.update(KEY_DLNUMBER, driverIn);
        capDoc.update(KEY_BOATINGID , boatIn)
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "User Info Updated!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent (getActivity(),AccountInfo.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error updating!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

        userDoc.update(KEY_EMAIL, emailIn);
        userDoc.update(KEY_PASSWORD, passIn);
        userDoc.update(KEY_NAME, nameIn);
        userDoc.update(KEY_PHONE, phoneIn)
                .addOnSuccessListener(new OnSuccessListener< Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getContext(), "User Info Updated!", Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(getActivity(), AccountInfo.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Error updating!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());
            }
        });

    }
}
