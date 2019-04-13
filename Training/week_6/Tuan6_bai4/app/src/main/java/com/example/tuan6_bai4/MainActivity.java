package com.example.tuan6_bai4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Spinner spinDm;
    EditText editma,editten;
    Button btnNhap;
    ListView lvSp;
    ArrayList<DanhMuc> arraySpinner=new ArrayList<DanhMuc>();
    ArrayAdapter<DanhMuc> adapterSpinner=null;
    ArrayList<SanPham>arrayListview=new ArrayList<SanPham>();
    ArrayAdapter<SanPham>adapterListview=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setControl();
        getWidgetsControl();
        fakeDataCatalog();
        setEvents();
    }

    public void setControl(){
        spinDm = findViewById(R.id.spDanhmuc);
        editma = findViewById(R.id.edtmaSP);
        editten = findViewById(R.id.edttenSP);
        btnNhap = findViewById(R.id.btnnhapSP);
        lvSp = findViewById(R.id.listView1);
    }

    public void setEvents(){
        btnNhap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                addProductForCatalog();
            }
        });
        spinDm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                loadListProductByCatalog(arraySpinner.get(arg2));
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });
    }

    private void getWidgetsControl()
    {
        adapterSpinner=new ArrayAdapter<DanhMuc>(this, android.R.layout.simple_spinner_item, arraySpinner);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinDm.setAdapter(adapterSpinner);
        adapterListview=new ArrayAdapter<SanPham>(this, android.R.layout.simple_list_item_1, arrayListview);
        lvSp.setAdapter(adapterListview);
    }
    private void fakeDataCatalog()
    {
        DanhMuc cat1=new DanhMuc("1", "HP");
        DanhMuc cat2=new DanhMuc("2", "Dell");
        DanhMuc cat3=new DanhMuc("3", "Macbook");
        arraySpinner.add(cat1);
        arraySpinner.add(cat2);
        arraySpinner.add(cat3);
        adapterSpinner.notifyDataSetChanged();
    }

    private void addProductForCatalog()
    {
        SanPham p=new SanPham();
        p.setId(editma.getText()+"");
        p.setName(editten.getText()+"");
        DanhMuc c= (DanhMuc) spinDm.getSelectedItem();
        c.addProduct(p);
        loadListProductByCatalog(c);
    }

    private void loadListProductByCatalog(DanhMuc c)
    {
        arrayListview.clear();
        arrayListview.addAll(c.getListProduct());
        adapterListview.notifyDataSetChanged();
    }
}
