package de.androidcrypto.nfcndefexamples;

import android.content.Context;
import android.content.Intent;
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
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.switchmaterial.SwitchMaterial;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {

    Button readNfc;
    com.google.android.material.textfield.TextInputLayout inputField1Decoration, inputField2Decoration, inputField3Decoration;
    com.google.android.material.textfield.TextInputEditText typeDescription, inputField1, inputField2, inputField3, resultNfcWriting;
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
        inputField2 = findViewById(R.id.etMainInputline2);
        inputField2Decoration = findViewById(R.id.etMainInputline2Decoration);
        inputField3 = findViewById(R.id.etMainInputline3);
        inputField3Decoration = findViewById(R.id.etMainInputline3Decoration);
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
                "Text", "URI", "Telefone number", "Coordinate", "Coordinate userinfo", "StreetView",
                "Address", "Google navigation", "Email", "Application", "Target address"};
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
                    case "URI": {
                        inputSchemeUri();
                        break;
                    }
                    case "StreetView": {
                        inputSchemeStreetview();
                        break;
                    }
                    case "Email": {
                        inputSchemeEmail();
                        break;
                    }
                    case "Telefone number": {
                        inputSchemeTelefoneNumber();
                        break;
                    }
                    case "Coordinate": {
                        inputSchemeCoordinate();
                        break;
                    }
                    case "Coordinate userinfo": {
                        inputSchemeCoordinateUserinfo();
                        break;
                    }
                    case "Address": {
                        inputSchemeAddress();
                        break;
                    }
                    case "Google navigation": {
                        inputSchemeGoogleNavigation();
                        break;
                    }
                    case "Application": {
                        inputSchemeApplication();
                        break;
                    }
                    default: {
                        hideAllInputFields();
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
        inputField3Decoration.setVisibility(View.GONE);
        addTimestampToData.setVisibility(View.GONE);
        resultNfcWriting.setVisibility(View.GONE);
    }

    private void inputSchemeText() {
        hideAllInputFields();
        String description = "writes a NDEF record with a line of text";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter a textline");
        typeDescription.setVisibility(View.VISIBLE);
        inputField1Decoration.setVisibility(View.VISIBLE);
        addTimestampToData.setVisibility(View.VISIBLE);
        resultNfcWriting.setVisibility(View.VISIBLE);
        inputField1.setText("sample text");
    }

    private void inputSchemeUri() {
        hideAllInputFields();
        String description = "writes a NDEF record with an URI";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter an URI including https://");
        typeDescription.setVisibility(View.VISIBLE);
        inputField1Decoration.setVisibility(View.VISIBLE);
        resultNfcWriting.setVisibility(View.VISIBLE);
        inputField1.setText("https://");
    }

    private void inputSchemeTelefoneNumber() {
        hideAllInputFields();
        String description = "writes a NDEF record with a telefone number";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter a telefone number");
        typeDescription.setVisibility(View.VISIBLE);
        inputField1Decoration.setVisibility(View.VISIBLE);
        resultNfcWriting.setVisibility(View.VISIBLE);
        inputField1.setText("0049201234567890");
    }

    private void inputSchemeEmail() {
        hideAllInputFields();
        String description = "writes a NDEF record with a complete Email";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter an email address for the recipient");
        inputField2Decoration.setHint("Enter the email subject");
        inputField3Decoration.setHint("Enter the email body");
        typeDescription.setVisibility(View.VISIBLE);
        inputField1Decoration.setVisibility(View.VISIBLE);
        inputField2Decoration.setVisibility(View.VISIBLE);
        inputField3Decoration.setVisibility(View.VISIBLE);
        addTimestampToData.setVisibility(View.VISIBLE);
        resultNfcWriting.setVisibility(View.VISIBLE);
        inputField1.setText("androidcrypto@gmx.de");
        inputField2.setText("sample email subject");
        inputField3.setText("Hello AndroidCrypto,\nThis is a sample mail.");
    }

    private void inputSchemeStreetview() {
        hideAllInputFields();
        String description = "writes a NDEF record with a Google streetview link";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter coordinates (comma separated)");
        typeDescription.setVisibility(View.VISIBLE);
        inputField1Decoration.setVisibility(View.VISIBLE);
        resultNfcWriting.setVisibility(View.VISIBLE);
        inputField1.setText("34.792345,-111.762531");
    }

    private void inputSchemeCoordinate() {
        hideAllInputFields();
        String description = "writes a NDEF record with a coordinate";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter coordinates (comma separated)");
        typeDescription.setVisibility(View.VISIBLE);
        inputField1Decoration.setVisibility(View.VISIBLE);
        resultNfcWriting.setVisibility(View.VISIBLE);
        inputField1.setText("34.792345,-111.762531");
    }

    private void inputSchemeCoordinateUserinfo() {
        hideAllInputFields();
        String description = "writes a NDEF record with a coordinate and user information";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter coordinates (comma separated)");
        inputField2Decoration.setHint("Enter the user information");
        typeDescription.setVisibility(View.VISIBLE);
        inputField1Decoration.setVisibility(View.VISIBLE);
        inputField2Decoration.setVisibility(View.VISIBLE);
        resultNfcWriting.setVisibility(View.VISIBLE);
        inputField1.setText("34.792345,-111.762531");
        inputField2.setText("Bell Rock Sedona view point");
    }

    private void inputSchemeAddress() {
        hideAllInputFields();
        String description = "writes a NDEF record with an address for Google maps";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter an street with (optional) house number");
        inputField2Decoration.setHint("Enter the zip code");
        inputField3Decoration.setHint("Enter the city");
        typeDescription.setVisibility(View.VISIBLE);
        inputField1Decoration.setVisibility(View.VISIBLE);
        inputField2Decoration.setVisibility(View.VISIBLE);
        inputField3Decoration.setVisibility(View.VISIBLE);
        resultNfcWriting.setVisibility(View.VISIBLE);
        inputField1.setText("Selmastr 5");
        inputField2.setText("45127");
        inputField3.setText("Essen");
    }

    private void inputSchemeGoogleNavigation() {
        hideAllInputFields();
        String description = "writes a NDEF record with a target address for Google navigation";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter an street with (optional) house number");
        inputField2Decoration.setHint("Enter the zip code");
        inputField3Decoration.setHint("Enter the city");
        typeDescription.setVisibility(View.VISIBLE);
        inputField1Decoration.setVisibility(View.VISIBLE);
        inputField2Decoration.setVisibility(View.VISIBLE);
        inputField3Decoration.setVisibility(View.VISIBLE);
        resultNfcWriting.setVisibility(View.VISIBLE);
        inputField1.setText("Selmastr 5");
        inputField2.setText("45127");
        inputField3.setText("Essen");
    }

    private void inputSchemeApplication() {
        hideAllInputFields();
        String description = "writes a NDEF record with an application to start";
        typeDescription.setText(description);
        inputField1Decoration.setHint("Enter a packet name");
        typeDescription.setVisibility(View.VISIBLE);
        inputField1Decoration.setVisibility(View.VISIBLE);
        resultNfcWriting.setVisibility(View.VISIBLE);
        inputField1.setText("com.inkwired.droidinfo");
    }

    private void showWirelessSettings() {
        Toast.makeText(this, "You need to enable NFC", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {

            if (!mNfcAdapter.isEnabled())
                showWirelessSettings();

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
            NdefRecord ndefRecord1;
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
                case "URI": {
                    String data = inputData1;
                    ndefRecord1 = NdefRecord.createUri(data);
                    ndefMessage = new NdefMessage(ndefRecord1);
                    break;
                }
                case "Telefone number": {
                    String data = inputData1;
                    String completeData = "tel:" + data;
                    ndefRecord1 = NdefRecord.createUri(completeData);
                    ndefMessage = new NdefMessage(ndefRecord1);
                    break;
                }
                case "Email": {
                    String data1 = inputData1;
                    String data2 = inputField2.getText().toString();
                    String data3 = inputField3.getText().toString();
                    String completeData = "mailto:" + Uri.encode(data1) + "?subject=" +
                            Uri.encode(data2);
                    if (addTimestamp) completeData = completeData + Uri.encode(" " + Utils.getTimestamp());
                    completeData = completeData + "&body=" + Uri.encode(data3);
                    ndefRecord1 = NdefRecord.createUri(completeData);
                    ndefMessage = new NdefMessage(ndefRecord1);
                    break;
                }
                case "StreetView": {
                    String data = inputData1;
                    String completeData = "google.streetview:cbll=" + data;
                    ndefRecord1 = NdefRecord.createUri(completeData);
                    ndefMessage = new NdefMessage(ndefRecord1);
                    break;
                }
                case "Coordinate": {
                    String data = inputData1;
                    String completeData = "geo:" + data;
                    ndefRecord1 = NdefRecord.createUri(completeData);
                    ndefMessage = new NdefMessage(ndefRecord1);
                    break;
                }
                case "Coordinate userinfo": {
                    String data1 = inputData1;
                    String data2 = Uri.encode(inputField2.getText().toString());
                    String completeData = "geo:0,0?q=" + data1 + "(" + data2 + ")";
                    ndefRecord1 = NdefRecord.createUri(completeData);
                    ndefMessage = new NdefMessage(ndefRecord1);
                    break;
                }
                case "Address": {
                    String data1 = Uri.encode(inputData1);
                    String data2 = Uri.encode(inputField2.getText().toString());
                    String data3 = Uri.encode(inputField3.getText().toString());
                    String completeData = "geo:0,0?q=" + data1 + "+" + data2 + "+" + data3;
                    ndefRecord1 = NdefRecord.createUri(completeData);
                    ndefMessage = new NdefMessage(ndefRecord1);
                    break;
                }
                case "Google navigation": {
                    String data1 = Uri.encode(inputData1);
                    String data2 = Uri.encode(inputField2.getText().toString());
                    String data3 = Uri.encode(inputField3.getText().toString());
                    String completeData = "google.navigation:q=" + data1 + "+" + data2 + "+" + data3;
                    ndefRecord1 = NdefRecord.createUri(completeData);
                    ndefMessage = new NdefMessage(ndefRecord1);
                    break;
                }
                case "Application": {
                    String data = inputData1;
                    ndefRecord1 = NdefRecord.createApplicationRecord(data);
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