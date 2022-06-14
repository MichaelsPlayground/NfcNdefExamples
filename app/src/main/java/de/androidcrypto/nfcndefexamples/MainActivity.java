package de.androidcrypto.nfcndefexamples;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.TagLostException;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {

    Button readNfc;
    com.google.android.material.textfield.TextInputLayout inputField1Decoration, inputField2Decoration;
    com.google.android.material.textfield.TextInputEditText typeDescription, inputField1, inputfield2, resultNfcWriting;
    SwitchMaterial addTimestampToData;
    AutoCompleteTextView autoCompleteTextView;

    Intent readNfcIntent;
    private NfcAdapter mNfcAdapter;

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
        resultNfcWriting = findViewById(R.id.etMainResult);
        addTimestampToData = findViewById(R.id.swMainAddTimestampSwitch);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        hideAllInputFields();

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

        autoCompleteTextView = findViewById(R.id.ndef_type);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String choiceString = autoCompleteTextView.getText().toString();
                //Toast.makeText(MainActivity.this, autoCompleteTextView.getText().toString(), Toast.LENGTH_SHORT).show();
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

    private void hideAllInputFields() {
        typeDescription.setVisibility(View.GONE);
        inputField1Decoration.setVisibility(View.GONE);
        inputField2Decoration.setVisibility(View.GONE);
        addTimestampToData.setVisibility(View.GONE);
        resultNfcWriting.setVisibility(View.GONE);
    }

    private void inputSchemeText() {
        String description = "writes a NDEF record with a line of text";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter a textline");
        inputField1Decoration.setVisibility(View.VISIBLE);
        inputField2Decoration.setVisibility(View.GONE);
        addTimestampToData.setVisibility(View.VISIBLE);
        resultNfcWriting.setVisibility(View.VISIBLE);
        inputField1.setText("sample text");
    }

    private void inputSchemeUrl() {
        String description = "writes a NDEF record with an URL";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter an URL including https...");
        inputField1Decoration.setVisibility(View.VISIBLE);
        inputField2Decoration.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            Bundle options = new Bundle();
            // Work around for some broken Nfc firmware implementations that poll the card too fast
            options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250);

            // Enable ReaderMode for all types of card and disable platform sounds
            // the option NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK is NOT set
            // to get the data of the tag afer reading
            mNfcAdapter.enableReaderMode(this,
                    this,
                    NfcAdapter.FLAG_READER_NFC_A |
                            NfcAdapter.FLAG_READER_NFC_B |
                            NfcAdapter.FLAG_READER_NFC_F |
                            NfcAdapter.FLAG_READER_NFC_V |
                            NfcAdapter.FLAG_READER_NFC_BARCODE |
                            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
                    options);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null)
            mNfcAdapter.disableReaderMode(this);
    }

    // This method is run in another thread when a card is discovered
    // !!!! This method cannot cannot direct interact with the UI Thread
    // Use `runOnUiThread` method to change the UI from this method
    @Override
    public void onTagDiscovered(Tag tag) {
        // Read and or write to Tag here to the appropriate Tag Technology type class
        // in this example the card should be an Ndef Technology Type

        Ndef mNdef = Ndef.get(tag);

        // Check that it is an Ndef capable card
        if (mNdef != null) {
            NdefMessage ndefMessage;
            NdefRecord ndefRecord1, ndefRecord2;
            // nfc ndef writing depends on the type
            String choiceString = autoCompleteTextView.getText().toString();
            String inputData1 = inputField1.getText().toString();
            boolean addTimestamp = addTimestampToData.isChecked();
            switch (choiceString) {
                case "Text": {
                    String data = inputData1;
                    if (addTimestamp) data = data + " " + Utils.getTimestamp();
                    ndefRecord1 = NdefRecord.createTextRecord("en", data);
                    ndefMessage = new NdefMessage(ndefRecord1);
                    break;
                }

                default:
                    throw new IllegalStateException("Unexpected value: " + choiceString);
            }


            // the tag is written here
            try {
                mNdef.connect();
                mNdef.writeNdefMessage(ndefMessage);
                // Success if got to here
                runOnUiThread(() -> {
                    resultNfcWriting.setText("write to NFC success");
                    Toast.makeText(getApplicationContext(),
                            "write to NFC success",
                            Toast.LENGTH_SHORT).show();
                });
            } catch (FormatException e) {
                runOnUiThread(() -> {
                    resultNfcWriting.setText("failure FormatException: " + e);
                    Toast.makeText(getApplicationContext(),
                            "FormatException: " + e,
                            Toast.LENGTH_SHORT).show();
                });
                // if the NDEF Message to write is malformed
            } catch (TagLostException e) {
                runOnUiThread(() -> {
                    resultNfcWriting.setText("failure TagLostException: " + e);
                    Toast.makeText(getApplicationContext(),
                            "TagLostException: " + e,
                            Toast.LENGTH_SHORT).show();
                });
                // Tag went out of range before operations were complete
            } catch (IOException e) {
                // if there is an I/O failure, or the operation is cancelled
                runOnUiThread(() -> {
                    resultNfcWriting.setText("failure IOException: " + e);
                    Toast.makeText(getApplicationContext(),
                            "IOException: " + e,
                            Toast.LENGTH_SHORT).show();
                });
            } finally {
                // Be nice and try and close the tag to
                // Disable I/O operations to the tag from this TagTechnology object, and release resources.
                try {
                    mNdef.close();
                } catch (IOException e) {
                    // if there is an I/O failure, or the operation is cancelled
                    runOnUiThread(() -> {
                        resultNfcWriting.setText("failure IOException: " + e);
                        Toast.makeText(getApplicationContext(),
                                "IOException: " + e,
                                Toast.LENGTH_SHORT).show();
                    });
                }
            }

            // Make a Sound
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150,10));
            } else {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(200);
            }



        } else {
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(),
                        "mNdef is null",
                        Toast.LENGTH_SHORT).show();
            });
        }
    }

}