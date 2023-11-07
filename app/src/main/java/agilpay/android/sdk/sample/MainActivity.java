package agilpay.android.sdk.sample;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.UUID;

import agilpay.mobile.sdk.android.PaymentServices;
import agilpay.mobile.sdk.android.abstraction.InitializationCallback;
import agilpay.mobile.sdk.android.abstraction.PaymentCallback;
import agilpay.mobile.sdk.android.models.PaymentRequest;
import agilpay.mobile.sdk.android.models.Transaction;

public class MainActivity extends AppCompatActivity {
    private ProgressBar progress;
    private EditText editCustomerId, editAmount, editTax, editCurrency, editEmail, editCustomerName, editInvoice, editClientId, editClientSecret, editMerchantKey, editApiUr;

    private CheckBox checkForceDuplicate, checkHoldFunds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("AgilPay Android SDK Sample");

        progress = findViewById(R.id.progress);
        editCustomerId = findViewById(R.id.editCustomerId);
        editAmount = findViewById(R.id.editAmount);
        editTax = findViewById(R.id.editTax);
        editCurrency = findViewById(R.id.editCurrency);
        editEmail = findViewById(R.id.editEmail);
        editCustomerName = findViewById(R.id.editCustomerName);
        editInvoice = findViewById(R.id.editInvoice);
        editClientId = findViewById(R.id.editClientId);
        editClientSecret = findViewById(R.id.editClientSecret);
        editMerchantKey = findViewById(R.id.editMerchantKey);
        editApiUr = findViewById(R.id.editApiUrl);
        checkForceDuplicate = findViewById(R.id.checkForceDuplicate);
        checkHoldFunds = findViewById(R.id.checkHoldFunds);

        editInvoice.setText(UUID.randomUUID().toString().substring(0,9).replaceAll("-", ""));

        findViewById(R.id.btnPay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                processPayment();
            }
        });

        Button btnViewCredentials = findViewById(R.id.btnViewCredentials);
        View layoutCredentials = findViewById(R.id.layoutCredentials);

        btnViewCredentials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutCredentials.setVisibility(layoutCredentials.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                btnViewCredentials.setText(layoutCredentials.getVisibility() == View.VISIBLE ? "Hide credentials" : "Show credentials");
            }
        });
    }


    private void initSDK(){
        if(!PaymentServices.isInitialized()){

            String apiUrl = editApiUr.getText().toString();
            String clientId = editClientId.getText().toString();
            String clientSecret = editClientSecret.getText().toString();


            progress.setVisibility(View.VISIBLE);
            PaymentServices.initialize(apiUrl, clientId, clientSecret, new InitializationCallback() {
                @Override
                public void onInitSuccess() {
                    progress.setVisibility(View.GONE);
                    processPayment();
                }

                @Override
                public void onInitFailed(String error) {
                    progress.setVisibility(View.GONE);
                    showAlert("Error", error);
                }
            });
        }
    }

    private void processPayment(){
        if(!PaymentServices.isInitialized()){
            initSDK();
            return;
        }

        String customerId = editCustomerId.getText().toString();
        String amountString = editAmount.getText().toString();
        String currency = editCurrency.getText().toString();
        String email = editEmail.getText().toString();
        String customerName = editCustomerName.getText().toString();
        String invoice = editInvoice.getText().toString();
        String merchantKey = editMerchantKey.getText().toString();
        String taxString = editTax.getText().toString();

        double amount;
        try{
            amount = Double.parseDouble(amountString);
        }catch(Exception e){
            showAlert("Info", "Please enter a valid amount");
            return;
        }

        double tax = 0.0;

        try{
            tax = Double.parseDouble(taxString);
        }catch(Exception e){
            e.printStackTrace();
            editTax.setText("0.0");
        }

        PaymentRequest request = new PaymentRequest();
        request.setCustomerId(customerId);
        request.setAmount(amount);
        request.setCurrency(currency);
        request.setCustomerEmail(email);
        request.setCustomerName(customerName);
        request.setInvoice(invoice);
        request.setMerchantKey(merchantKey);
        request.setTax(tax);
        request.setForceDuplicate(checkForceDuplicate.isChecked());
        request.setHoldFunds(checkHoldFunds.isChecked());

        PaymentServices.processPayment(MainActivity.this, request, new PaymentCallback() {
            @Override
            public void onPaymentResult(Transaction transaction) {

                Log.d("TAG", transaction.toString());
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    private void showAlert(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", null)
                .show();
    }
}