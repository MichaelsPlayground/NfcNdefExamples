package de.androidcrypto.nfcndefexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button readNfc;
    com.google.android.material.textfield.TextInputLayout inputField1Decoration, inputField2Decoration;
    com.google.android.material.textfield.TextInputEditText typeDescription, inputField1, inputfield2;
    Intent readNfcIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readNfc = findViewById(R.id.btnMainReadNfcNdefTag);
        readNfcIntent = new Intent(MainActivity.this, ReadNdefActivity.class);

        typeDescription = findViewById(R.id.etMainTypeDescription);
        inputField1 = findViewById(R.id.etMainInputline1);
        inputField1Decoration = findViewById(R.id.etMainInputline1Decoration);
        inputfield2 = findViewById(R.id.etMainInputline2);
        inputField2Decoration = findViewById(R.id.etMainInputline2Decoration);

        readNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(readNfcIntent);
            }
        });

        String[] type = new String[]{
                "Text", "URL", "Dateilink", "Videolink", "Email address", "Telefone number", "Target address", "StreetView", "Emergency informations"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                R.layout.drop_down_item,
                type);

        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.ndef_type);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String choiceString = autoCompleteTextView.getText().toString();
                Toast.makeText(MainActivity.this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
                switch (choiceString) {
                    case "Text": {
                        inputSchemeText();
                        break;
                    }
                    case "URL": {
                        inputSchemeUrl();
                        break;
                    }
                    case "Streetview": {

                        break;
                    }
                    default: {

                        break;
                    }
                }

            }
        });

    }

    private void inputSchemeText() {
        String description = "writes a NDEF record with a line of text";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter a textline");
        inputField1Decoration.setVisibility(View.VISIBLE);
        inputField2Decoration.setVisibility(View.GONE);


    }

    private void inputSchemeUrl() {
        String description = "writes a NDEF record with an URL";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter an URL including https...");
        inputField1Decoration.setVisibility(View.VISIBLE);
        inputField2Decoration.setVisibility(View.GONE);


    }
}