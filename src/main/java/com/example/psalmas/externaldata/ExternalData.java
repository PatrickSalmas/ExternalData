package com.example.psalmas.externaldata;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class ExternalData extends AppCompatActivity implements OnItemSelectedListener, OnClickListener {

    private TextView canWrite, canRead;
    private String state;
    boolean canW, canR;
    Spinner spinner;
    String[] paths = {"Music","Pictures", "Downloads"};
    File filePath = null;
    File file = null;
    EditText saveFile;
    Button confirm, save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_data);
        canWrite = (TextView) findViewById(R.id.tvCanWrite);
        canRead = (TextView) findViewById(R.id.tvCanRead);
        canWrite.setText("false");
        canRead.setText("false");
        confirm = (Button) findViewById(R.id.btConfirmSaveAs);
        save = (Button) findViewById(R.id.btSaveFile);
        saveFile = (EditText) findViewById(R.id.etSaveAs);
        confirm.setOnClickListener(this);
        save.setOnClickListener(this);

        checkState();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ExternalData.this, android.R.layout.simple_spinner_item, paths);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(ExternalData.this);

    }

    private void checkState() {

        state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            //read and write
            canWrite.setText("true");
            canRead.setText("true");
            canW = canR = true;

        } else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            //read only
            canWrite.setText("false");
            canRead.setText("true");
            canW = false;
            canR = true;
        } else {
            canWrite.setText("false");
            canRead.setText("false");
            canW = canR = false;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        int position = spinner.getSelectedItemPosition();
        switch (position) {
            case 0:
                filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
                break;
            case 1:
                filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                break;
            case 2:
                filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btSaveFile:
                String strFile = saveFile.getText().toString();
                file = new File(filePath, strFile);
                checkState();
                if (canW && canR) {
                    filePath.mkdirs();

                    try {
                        InputStream inputStream = getResources().openRawResource(R.raw.greenball);
                        OutputStream outputStream = new FileOutputStream(file);
                        byte[] data = new byte[inputStream.available()];
                        inputStream.read(data);
                        outputStream.write(data);
                        inputStream.close();
                        outputStream.close();

                        Toast toast = Toast.makeText(ExternalData.this, "File has been saved", Toast.LENGTH_LONG);
                        toast.show();

                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    catch (IOException e) {
                            e.printStackTrace();
                    }
                }
                break;
            case R.id.btConfirmSaveAs:
                save.setVisibility(View.VISIBLE);
                break;
        }
    }
}
