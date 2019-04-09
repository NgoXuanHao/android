package com.example.tuan_6_bai_5;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MySMSActivity extends AppCompatActivity {
    Button btnSendSMS;
    EditText editContent;
    TextView txtSendTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_sms);

        setControl();
        setEvents();
    }

    public void setControl() {
        btnSendSMS = findViewById(R.id.btnSendSms);
        editContent = findViewById(R.id.editSMS);
        txtSendTo = findViewById(R.id.txtSendTo);
    }

    public void setEvents() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("DATA");
        final contact contact = (contact) bundle.getSerializable("CONTACT");
        btnSendSMS.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendSms(contact);
            }
        });
        txtSendTo.setText("Send to : " + contact.getNumberContact());
    }

    public void sendSms(contact contact) {
        final SmsManager smsManager = SmsManager.getDefault();
        Intent msgSent = new Intent("ACTION_MSG_SENT");
        final PendingIntent pendingMsgSent = PendingIntent.getBroadcast(this, 0, msgSent, 0);
        registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int result = getResultCode();
                String msg = "Send OK";
                if (result != Activity.RESULT_OK) {
                    msg = "Send failed";
                }
                Toast.makeText(MySMSActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        }, new IntentFilter("ACTION_MSG_SENT"));
        smsManager.sendTextMessage(contact.getNumberContact(), null, editContent.getText() + "", pendingMsgSent, null);
        finish();
    }
}


