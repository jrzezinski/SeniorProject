// By Justin Rzezinski

package group.project.buberapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.HttpResponseCallback;
import com.braintreepayments.api.internal.HttpClient;
import com.braintreepayments.api.models.PaymentMethodNonce;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener {

    private static final int REQUEST_CODE = 1234;

    // Server information
    final String API_GET_TOKEN = "http://18.211.178.129:80/braintree/main.php";
    final String API_CHECK_OUT = "http://18.211.178.129:80/braintree/checkout.php";

    // Declare variables
    String token,amount;
    HashMap<String,String> paramsHash;
    Button payButton;
    TextView paymentAmount;
    RelativeLayout group_waiting,group_payment;
    String finalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // initialize variables
        group_payment = (RelativeLayout) findViewById(R.id.payment_group);
        group_waiting = (RelativeLayout) findViewById(R.id.waiting_group);
        paymentAmount = (TextView)findViewById(R.id.payment_amount);
        payButton = (Button) findViewById(R.id.payment_button);
        finalCost = getIntent().getStringExtra("EXTRA_Final_COST");

        paymentAmount.setText(finalCost);

        // getToken from server
        new getToken().execute();

        // Listen for button click event
        payButton.setOnClickListener(new View.OnClickListener()
                                       {
                                            @Override
                                            public void onClick(View view)
                                            {
                                                // run payment method
                                                submitPayment();
                                            }
                                       }
                                   );
    }

    // Payment api from braintree
    private void submitPayment()
    {
        DropInRequest dropInRequest = new DropInRequest().clientToken(token);
        startActivityForResult(dropInRequest.getIntent(this),REQUEST_CODE);

    }

    // react to payment info from braintree api
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        if(requestCode == REQUEST_CODE)
        {
            // Entry goes through
            if (resultCode == RESULT_OK)
            {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strNonce = nonce.getNonce();

                // send info if amount given
                if(!paymentAmount.getText().toString().isEmpty())
                {
                    // get payment amount from user field and nonce
                    amount = paymentAmount.getText().toString();
                    paramsHash = new HashMap<>();
                    paramsHash.put("amount",amount);
                    paramsHash.put("nonce",strNonce);

                    // call method to send payment info to server
                    sendPayments();
                }
                else
                {
                    Toast.makeText(this, "Please enter valid amount", Toast.LENGTH_SHORT).show();
                }

            }
            // if the user cancels the entry
            else if(resultCode == RESULT_CANCELED)
            {
                Toast.makeText(this, "User Canceled", Toast.LENGTH_SHORT).show();
            }
            // other error
            else
            {
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("EDMT_ERROR",error.toString());
            }
        }
    }

    // send payment info to server
    private void sendPayments() {
        RequestQueue queue = Volley.newRequestQueue(PaymentActivity.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, API_CHECK_OUT,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        if(response.toString().contains("Successful"))
                        {
                            Toast.makeText(PaymentActivity.this, "Transaction successful!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(PaymentActivity.this, "Transaction failed!", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("EDMT_LOG",response.toString());
                    }
                }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("EDMT_ERROR",error.toString());
                        }
                    })
                        {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                if(paramsHash == null)
                                {
                                    return null;
                                }

                                Map<String,String> params = new HashMap<>();
                                for(String key:paramsHash.keySet())
                                {
                                    params.put(key,paramsHash.get(key));
                                }
                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError
                            {
                                Map<String,String> params = new HashMap<>();
                                params.put("Content-Type","application/x-www-form-urlencoded");
                                return params;
                            }
                        };

        queue.add(stringRequest);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    private class getToken extends AsyncTask
    {

        ProgressDialog mDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            mDialog = new ProgressDialog(PaymentActivity.this,android.R.style.Theme_DeviceDefault_Dialog);
            mDialog.setCancelable(false);
            mDialog.setMessage("Please wait");
            mDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects)
        {
            HttpClient client = new HttpClient();

            client.get(API_GET_TOKEN, new HttpResponseCallback()
            {
                @Override
                public void success(final String responseBody)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Hide group waiting
                            group_waiting.setVisibility(View.GONE);
                            //Show group payment
                            group_payment.setVisibility(View.VISIBLE);
                            //Set token
                            token = responseBody;
                        }
                    });
                }

                @Override
                public void failure(Exception exception)
                {
                    Log.d("EDMT_ERROR",exception.toString());

                }

            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o)
        {
            super.onPostExecute(o);
            mDialog.dismiss();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
