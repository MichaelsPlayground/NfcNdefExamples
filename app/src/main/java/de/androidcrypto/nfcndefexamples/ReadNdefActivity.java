package de.androidcrypto.nfcndefexamples;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class ReadNdefActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {

    TextView nfcContentParsed, nfcContentRaw;

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_ndef);
        nfcContentParsed = findViewById(R.id.tvReadNdefContentParsed);
        nfcContentRaw = findViewById(R.id.tvReadNdefContentRaw);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
    }

    // This method is run in another thread when a card is discovered
    // !!!! This method cannot cannot direct interact with the UI Thread
    // Use `runOnUiThread` method to change the UI from this method
    @Override
    public void onTagDiscovered(Tag tag) {
        // Read and or write to Tag here to the appropriate Tag Technology type class
        // in this example the card should be an Ndef Technology Type

        System.out.println("NFC tag discovered");
        Ndef mNdef = Ndef.get(tag);

        // Check that it is an Ndef capable card
        if (mNdef != null) {

            // If we want to read
            // As we did not turn on the NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
            // We can get the cached Ndef message the system read for us.

            NdefMessage mNdefMessage = mNdef.getCachedNdefMessage();

            // Make a vibration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150,10));
            } else {
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(200);
            }

            NdefRecord[] record = mNdefMessage.getRecords();
            String ndefContent = "raw data\n";
            int ndefRecordsCount = record.length;
            ndefContent = ndefContent + "nr of records: " + ndefRecordsCount + "\n";
            // Success if got to here
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(),
                        "Read from NFC success, number of records: " + ndefRecordsCount,
                        Toast.LENGTH_SHORT).show();
            });

            // get uid of the tag
            ndefContent = ndefContent + "UID: " + bytesToHex(mNdef.getTag().getId()) + "\n";
            // get the techlist = support technoligies of the tag, e.g. android.nfc.tech.Nfca
            ndefContent = ndefContent + "TecList: " + Arrays.toString(mNdef.getTag().getTechList()) + "\n";

            if (ndefRecordsCount > 0) {
                String ndefText = "";
                for (int i = 0; i < ndefRecordsCount; i++) {
                    short ndefTnf = record[i].getTnf();

                    switch (ndefTnf) {
                        case NdefRecord.TNF_EMPTY: {
                            ndefContent = ndefContent + "\n" + "rec: " + i +
                                    " TNF: " + ndefTnf + " (0 TNF_EMPTY)";
                            break;
                        }
                        case NdefRecord.TNF_WELL_KNOWN: {
                            ndefContent = ndefContent + "\n" + "rec: " + i +
                                    " TNF: " + ndefTnf + " (1 TNF_WELL_KNOWN)";
                            break;
                        }
                        case NdefRecord.TNF_MIME_MEDIA: {
                            ndefContent = ndefContent + "\n" + "rec: " + i +
                                    " TNF: " + ndefTnf + " (2 TNF_MIME_MEDIA)";
                            break;
                        }
                        case NdefRecord.TNF_ABSOLUTE_URI: {
                            ndefContent = ndefContent + "\n" + "rec: " + i +
                                    " TNF: " + ndefTnf + " (3 TNF_ABSOLUTE_URI)";
                            break;
                        }
                        case NdefRecord.TNF_EXTERNAL_TYPE: {
                            ndefContent = ndefContent + "\n" + "rec: " + i +
                                    " TNF: " + ndefTnf + " (4 TNF_EXTERNAL_TYPE)";
                            break;
                        }
                        case NdefRecord.TNF_UNKNOWN: {
                            ndefContent = ndefContent + "\n" + "rec: " + i +
                                    " TNF: " + ndefTnf + " (5 TNF_UNKNOWN)";
                            break;
                        }
                        case NdefRecord.TNF_UNCHANGED: {
                            ndefContent = ndefContent + "\n" + "rec: " + i +
                                    " TNF: " + ndefTnf + " (6 TNF_UNCHANGED)";
                            break;
                        }
                        default: {
                            ndefContent = ndefContent + "\n" + "rec: " + i +
                                    " TNF: " + ndefTnf + " (undefined)";
                            break;
                        }
                    }

                    byte[] ndefType = record[i].getType();
                    byte[] ndefPayload = record[i].getPayload();

                    ndefContent = ndefContent + "\n" + "rec " + i + " inf: " + ndefTnf +
                            " type: " + bytesToHex(ndefType) +
                            " payload: " + bytesToHex(ndefPayload) +
                            " \n" + new String(ndefPayload) + " \n";
                    String finalNdefContent = ndefContent;
                    runOnUiThread(() -> {
                        nfcContentRaw.setText(finalNdefContent);
                        System.out.println(finalNdefContent);
                    });

                    // here we are trying to parse the content
                    // Well known type - Text
                    if (ndefTnf == NdefRecord.TNF_WELL_KNOWN &&
                            Arrays.equals(ndefType, NdefRecord.RTD_TEXT)) {
                        ndefText = ndefText + "\n" + "rec: " + i +
                                " Well known Text payload\n" + new String(ndefPayload) + " \n";
                        ndefText = ndefText + Utils.parseTextrecordPayload(ndefPayload) + " \n";
                    }
                    // Well known type - Uri
                    if (ndefTnf == NdefRecord.TNF_WELL_KNOWN &&
                            Arrays.equals(ndefType, NdefRecord.RTD_URI)) {
                        ndefText = ndefText + "\n" + "rec: " + i +
                                " Well known Uri payload\n" + new String(ndefPayload) + " \n";
                        ndefText = ndefText + Utils.parseUrirecordPayload(ndefPayload) + " \n";
                    }

                    // TNF 2 Mime Media
                    if (ndefTnf == NdefRecord.TNF_MIME_MEDIA) {
                        ndefText = ndefText + "\n" + "rec: " + i +
                                " TNF Mime Media  payload\n" + new String(ndefPayload) + " \n";
                        ndefText = ndefText + "TNF Mime Media  type\n" + new String(ndefType) + " \n";
                    }
                    // TNF 4 External type
                    if (ndefTnf == NdefRecord.TNF_EXTERNAL_TYPE) {
                        ndefText = ndefText + "\n" + "rec: " + i +
                                " TNF External type payload\n" + new String(ndefPayload) + " \n";
                        ndefText = ndefText + "TNF External type type\n" + new String(ndefType) + " \n";
                    }
                    String finalNdefText = ndefText;
                    runOnUiThread(() -> {
                        nfcContentParsed.setText(finalNdefText);
                        System.out.println(finalNdefText);
                    });
                } // for
            }
        } else {
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(),
                        "mNdef is null",
                        Toast.LENGTH_SHORT).show();
            });
        }
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
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
}