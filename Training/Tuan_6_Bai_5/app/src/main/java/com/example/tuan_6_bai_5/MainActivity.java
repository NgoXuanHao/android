package com.example.tuan_6_bai_5;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ContextMenu;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText name;
    private EditText number;
    private Button submit;
    private ListView listView = null;
    private TextView info;


    ArrayList<contact> data = new ArrayList<>();
    contactAdapter adapter = null;
    contact selectdContact = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        setEvent();

    }

    public void setControl() {
        name = findViewById(R.id.name);
        number = findViewById(R.id.number);
        submit = findViewById(R.id.btnSubmit);
        listView = findViewById(R.id.listView);
        info = findViewById(R.id.contact);
        listView.setAdapter(adapter);
        registerForContextMenu(listView);

    }

    public void setEvent() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveContact();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                selectdContact = data.get(position);

                return false;
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {

        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.phonecontextmenu, menu);
        menu.setHeaderTitle("Call- Sms");
        menu.getItem(0).setTitle("Call to " + selectdContact.getNumberContact());
        menu.getItem(1).setTitle("Send sms to " + selectdContact.getNumberContact());
    }

    public void saveContact() {
        String getName = name.getText() + "";
        String getNumber = number.getText() + "";

        data.add(new contact(getName,getNumber));
        adapter = new contactAdapter(this, R.layout.row, data);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        name.setText("");
        number.setText("");
    }

    public void makePhoneCall() {
        Uri uri = Uri.parse("tel:" + selectdContact.getNumberContact());
        Intent i = new Intent(Intent.ACTION_CALL, uri);
        startActivity(i);
    }

    public void makeSms() {
        Intent i = new Intent(MainActivity.this, MySMSActivity.class);
        Bundle b = new Bundle();
        b.putSerializable("CONTACT", selectdContact);
        Intent intent = i.putExtra("DATA", b);
        startActivity(intent);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnuCall:
                makePhoneCall();
                break;
            case R.id.mnuSms:
                makeSms();
                break;
            case R.id.mnuRemove:
                data.remove(selectdContact);
                adapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }

}
