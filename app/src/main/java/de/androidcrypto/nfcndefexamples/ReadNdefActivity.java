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

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import de.androidcrypto.nfcndefexamples.ndef.wellknown.TextRecord;

public class ReadNdefActivity extends AppCompatActivity implements NfcAdapter.ReaderCallback {

    TextView nfcContentHex, nfcContentString;

    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_ndef);
        nfcContentHex = findViewById(R.id.tvReadNdefContentHex);
        nfcContentString = findViewById(R.id.tvReadNdefContentString);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

    }

    // This method is run in another thread when a card is discovered
    // !!!! This method cannot cannot direct interact with the UI Thread
    // Use `runOnUiThread` method to change the UI from this method
    @Override
    public void onTagDiscovered(Tag tag) {
        // Read and or write to Tag here to the appropriate Tag Technology type class
        // in this example the card should be an Ndef Technology Type

        // clear the datafields
        //clearEncryptionData();

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
            String ndefContent = "";
            int ndefRecordsCount = record.length;
            ndefContent = "nr of records: " + ndefRecordsCount + "\n";
            // Success if got to here
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(),
                        "Read from NFC success, number of records: " + ndefRecordsCount,
                        Toast.LENGTH_SHORT).show();
            });

            if (ndefRecordsCount > 0) {
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
                    // check for encrypted content in an External NDEF message
                    short ndefTnf4 = (short) 4;
                    if (Short.compare(ndefTnf, ndefTnf4) == 0) {
                        // this is a record type 4
                        /*
                        byte[] saltDefinition = "de.androidcrypto.aes256gcmpbkdf2:salt".getBytes(StandardCharsets.UTF_8);
                        byte[] nonceDefinition = "de.androidcrypto.aes256gcmpbkdf2:nonce".getBytes(StandardCharsets.UTF_8);
                        byte[] ciphertextDefinition = "de.androidcrypto.aes256gcmpbkdf2:ciphertext".getBytes(StandardCharsets.UTF_8);
                        // checking for salt
                        if (Arrays.equals(ndefType, saltDefinition)) {
                            // salt definition found
                            saltBytes = Arrays.copyOf(ndefPayload, ndefPayload.length);
                        }
                        if (Arrays.equals(ndefType, nonceDefinition)) {
                            // nonce definition found
                            nonceBytes = Arrays.copyOf(ndefPayload, ndefPayload.length);
                        }
                        if (Arrays.equals(ndefType, ciphertextDefinition)) {
                            // ciphertext definition found
                            ciphertextBytes = Arrays.copyOf(ndefPayload, ndefPayload.length);
                        }*/
                    }

                    ndefContent = ndefContent + "\n" + "rec " + i + " inf: " + ndefTnf +
                            " type: " + bytesToHex(ndefType) +
                            " payload: " + bytesToHex(ndefPayload) +
                            " \n" + new String(ndefPayload) + " \n";
                    String finalNdefContent = ndefContent;
                    runOnUiThread(() -> {
                        nfcContentString.setText(finalNdefContent);
                        System.out.println(finalNdefContent);
                    });

                    if (ndefTnf == NdefRecord.TNF_WELL_KNOWN &&
                            Arrays.equals(ndefType, "T".getBytes(StandardCharsets.US_ASCII))) {
                        String ndefText = "XX payload\n" + new String(ndefPayload) + " \n";
                        TextRecord textRecord = new TextRecord(new String(ndefPayload));
                        String dataTextRecord = textRecord.getText();
                        ndefText = ndefText + dataTextRecord;
                        String finalNdefText = ndefText;
                        runOnUiThread(() -> {
                            nfcContentHex.setText(finalNdefText);
                            System.out.println(finalNdefText);
                        });
                    }

                }
            }
        } else {
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(),
                        "mNdef is null",
                        Toast.LENGTH_SHORT).show();
            });
        }


        /*
        NfcA nfca = null;

        // Whole process is put into a big try-catch trying to catch the transceive's IOException
        try {
            nfca = NfcA.get(tag);
            if (nfca != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(150,10));
                } else {
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(200);
                }
            }

            nfca.connect();
            byte[] response;
            String nfcaContentString = "Content of NFCA tag";

            // first get sak
            short sakData = nfca.getSak();
            nfcaContentString = nfcaContentString + "\n" + "read SAK";
            //nfcaContentString = nfcaContentString + "\n" + "sakData: " + shortToHex(sakData);

            // then check atqa
            byte[] atqaData = nfca.getAtqa();
            nfcaContentString = nfcaContentString + "\n" + "read ATQA";
            nfcaContentString = nfcaContentString + "\n" + "atqaData: " + bytesToHex(atqaData);

            // read complete storage = 888 bytes for NTAG216
            // todo identify tag

            //tagContentReadComplete = false;
            nfcaContentString = nfcaContentString + "\n" + "complete DUMP of NTAG216";
            int nrOfBlocksToRead = 222; // on block has 4 bytes
            int nrOfPagesToRead = nrOfBlocksToRead / 4; // with each read command 4 blocks got read = 16 byte
            int nrOfBytesCapacity = 888;
            int nrOfBytesArray = 1000; // we do need more space in the array as we read more data than available
            int bytesStored = 0;
            byte[] tagRead = new byte[nrOfBytesArray];
            for (int i = 0; i < nrOfPagesToRead; i++) {
                int blockNumber = i * 4; // reads 4 block at once
                byte[] result = nfca.transceive(new byte[] {
                        (byte)0x30,  // READ
                        (byte)(blockNumber & 0x0ff)
                });
                //nfcaContentString = nfcaContentString + "\n" + "i:" + i + " " + bytesToHex(result);
                //nfcaContentString = nfcaContentString + "\n" + "i:" + i + " " + new String(result);
                System.arraycopy(result, 0, tagRead, bytesStored, result.length);
                bytesStored = bytesStored + result.length;
            }
            // now get only the data that is maximal on the tag*/
            /*
            tagContent = new byte[nrOfBytesCapacity];
            tagContent = Arrays.copyOfRange(tagRead, 0, nrOfBytesCapacity);
            String tagDump = HexDumpUtil.formatHexDump(tagContent, 0, tagContent.length);
            tagContentString = tagDump;
            nfcaContentString = nfcaContentString + "\n" + "tagContent length: " + bytesStored;
            //nfcaContentString = nfcaContentString + "\n" + bytesToHex(tagContent);
            nfcaContentString = nfcaContentString + "\n" + tagDump;

            int responseLength;
            // Get Page 04h
            // reads 16 bytes = 4 pages in one run
            response = nfca.transceive(new byte[] {
                    (byte) 0x30, // READ
                    //(byte) 0x2A  // page address
                    (byte) 0x04  // page address
            });
            responseLength = response.length;
            nfcaContentString = nfcaContentString + "\n" + "read block 04h";
            nfcaContentString = nfcaContentString + "\n" + "responseLength: " + responseLength;
            nfcaContentString = nfcaContentString + "\n" + " d: " + bytesToHex(response);

            // Get Page 08h
            response = nfca.transceive(new byte[] {
                    (byte) 0x30, // READ
                    //(byte) 0x2A  // page address
                    (byte) 0x08  // page address
            });
            responseLength = response.length;
            nfcaContentString = nfcaContentString + "\n" + "read block 08h";
            nfcaContentString = nfcaContentString + "\n" + "responseLength: " + responseLength;
            nfcaContentString = nfcaContentString + "\n" + " d: " + bytesToHex(response);

            // Get Page E1h E1 = 225d is the last data page of a NTAG216, followed by 5 blocks of internal data
            response = nfca.transceive(new byte[] {
                    (byte) 0x30, // READ
                    //(byte) 0x2A  // page address
                    (byte) 0xE1  // page address
            });
            responseLength = response.length;
            nfcaContentString = nfcaContentString + "\n" + "read block E1h";
            nfcaContentString = nfcaContentString + "\n" + "responseLength: " + responseLength;
            nfcaContentString = nfcaContentString + "\n" + " d: " + bytesToHex(response);

            String finalNfcaContentString = nfcaContentString;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //UI related things, not important for NFC
                    nfcContentString.setText(finalNfcaContentString);
                }
            });*/

            /*
            // Generate NdefMessage to be written onto the tag
            NdefMessage msg = null;
            try {
                NdefRecord r1 = NdefRecord.createMime("text/plain", message.getBytes("UTF-8"));
                NdefRecord r2 = NdefRecord.createApplicationRecord("com.example.alex.nfcapppcekunde");
                msg = new NdefMessage(r1, r2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            byte[] ndefMessage = msg.toByteArray();

            nfca.transceive(new byte[] {
                    (byte)0xA2, // WRITE
                    (byte)3,    // block address
                    (byte)0xE1, (byte)0x10, (byte)0x12, (byte)0x00
            });

            // wrap into TLV structure
            byte[] tlvEncodedData = null;

            tlvEncodedData = new byte[ndefMessage.length + 3];
            tlvEncodedData[0] = (byte)0x03;  // NDEF TLV tag
            tlvEncodedData[1] = (byte)(ndefMessage.length & 0x0FF);  // NDEF TLV length (1 byte)
            System.arraycopy(ndefMessage, 0, tlvEncodedData, 2, ndefMessage.length);
            tlvEncodedData[2 + ndefMessage.length] = (byte)0xFE;  // Terminator TLV tag

            // fill up with zeros to block boundary:
            tlvEncodedData = Arrays.copyOf(tlvEncodedData, (tlvEncodedData.length / 4 + 1) * 4);
            for (int i = 0; i < tlvEncodedData.length; i += 4) {
                byte[] command = new byte[] {
                        (byte)0xA2, // WRITE
                        (byte)((4 + i / 4) & 0x0FF), // block address
                        0, 0, 0, 0
                };
                System.arraycopy(tlvEncodedData, i, command, 2, 4);
                try {
                    response = nfca.transceive(command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //UI related things, not important for NFC
                    btn.setImageResource(R.drawable.arrow_red);
                    tv.setText("");
                }
            });
            curAction = "handle";

            try {
                nfca.close();
                //tagContentReadComplete = true; // this is the right place to say it is complete
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            //Trying to catch any ioexception that may be thrown
            e.printStackTrace();
        } catch (Exception e) {
            //Trying to catch any exception that may be thrown
            e.printStackTrace();
        }
        /*
        // source: https://stackoverflow.com/questions/45502768/nfca-transceivebyte-data-throws-taglostexception
        // reading AND writing data
        NfcA nfca = null;

        // Whole process is put into a big try-catch trying to catch the transceive's IOException
        try {
            nfca = NfcA.get(tag);

            nfca.connect();

            byte[] response;

            // Authenticate with the tag first
            // In case it's already been locked
            try {
                response = nfca.transceive(new byte[]{
                        (byte) 0x1B, // PWD_AUTH
                        pwd[0], pwd[1], pwd[2], pwd[3]
                });

                // Check if PACK is matching expected PACK
                // This is a (not that) secure method to check if tag is genuine
                if ((response != null) && (response.length >= 2)) {
                    byte[] packResponse = Arrays.copyOf(response, 2);
                    if (!(pack[0] == packResponse[0] && pack[1] == packResponse[1])) {
                        Toast.makeText(ctx, "Tag could not be authenticated:\n" + packResponse.toString() + "≠" + pack.toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }catch(TagLostException e){
                e.printStackTrace();
            }

            // Get Page 2Ah
            response = nfca.transceive(new byte[] {
                    (byte) 0x30, // READ
                    (byte) 0x2A  // page address
            });
            // configure tag as write-protected with unlimited authentication tries
            if ((response != null) && (response.length >= 16)) {    // read always returns 4 pages
                boolean prot = false;                               // false = PWD_AUTH for write only, true = PWD_AUTH for read and write
                int authlim = 0;                                    // 0 = unlimited tries
                nfca.transceive(new byte[] {
                        (byte) 0xA2, // WRITE
                        (byte) 0x2A, // page address
                        (byte) ((response[0] & 0x078) | (prot ? 0x080 : 0x000) | (authlim & 0x007)),    // set ACCESS byte according to our settings
                        0, 0, 0                                                                         // fill rest as zeros as stated in datasheet (RFUI must be set as 0b)
                });
            }
            // Get page 29h
            response = nfca.transceive(new byte[] {
                    (byte) 0x30, // READ
                    (byte) 0x29  // page address
            });
            // Configure tag to protect entire storage (page 0 and above)
            if ((response != null) && (response.length >= 16)) {  // read always returns 4 pages
                int auth0 = 0;                                    // first page to be protected
                nfca.transceive(new byte[] {
                        (byte) 0xA2, // WRITE
                        (byte) 0x29, // page address
                        response[0], 0, response[2],              // Keep old mirror values and write 0 in RFUI byte as stated in datasheet
                        (byte) (auth0 & 0x0ff)
                });
            }

            // Send PACK and PWD
            // set PACK:
            nfca.transceive(new byte[] {
                    (byte)0xA2,
                    (byte)0x2C,
                    pack[0], pack[1], 0, 0  // Write PACK into first 2 Bytes and 0 in RFUI bytes
            });
            // set PWD:
            nfca.transceive(new byte[] {
                    (byte)0xA2,
                    (byte)0x2B,
                    pwd[0], pwd[1], pwd[2], pwd[3] // Write all 4 PWD bytes into Page 43
            });

            // Generate NdefMessage to be written onto the tag
            NdefMessage msg = null;
            try {
                NdefRecord r1 = NdefRecord.createMime("text/plain", message.getBytes("UTF-8"));
                NdefRecord r2 = NdefRecord.createApplicationRecord("com.example.alex.nfcapppcekunde");
                msg = new NdefMessage(r1, r2);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            byte[] ndefMessage = msg.toByteArray();

            nfca.transceive(new byte[] {
                    (byte)0xA2, // WRITE
                    (byte)3,    // block address
                    (byte)0xE1, (byte)0x10, (byte)0x12, (byte)0x00
            });

            // wrap into TLV structure
            byte[] tlvEncodedData = null;

            tlvEncodedData = new byte[ndefMessage.length + 3];
            tlvEncodedData[0] = (byte)0x03;  // NDEF TLV tag
            tlvEncodedData[1] = (byte)(ndefMessage.length & 0x0FF);  // NDEF TLV length (1 byte)
            System.arraycopy(ndefMessage, 0, tlvEncodedData, 2, ndefMessage.length);
            tlvEncodedData[2 + ndefMessage.length] = (byte)0xFE;  // Terminator TLV tag

            // fill up with zeros to block boundary:
            tlvEncodedData = Arrays.copyOf(tlvEncodedData, (tlvEncodedData.length / 4 + 1) * 4);
            for (int i = 0; i < tlvEncodedData.length; i += 4) {
                byte[] command = new byte[] {
                        (byte)0xA2, // WRITE
                        (byte)((4 + i / 4) & 0x0FF), // block address
                        0, 0, 0, 0
                };
                System.arraycopy(tlvEncodedData, i, command, 2, 4);
                try {
                    response = nfca.transceive(command);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //UI related things, not important for NFC
                    btn.setImageResource(R.drawable.arrow_red);
                    tv.setText("");
                }
            });
            curAction = "handle";

            try {
                nfca.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            //Trying to catch any ioexception that may be thrown
            e.printStackTrace();
        } catch (Exception e) {
            //Trying to catch any exception that may be thrown
            e.printStackTrace();
        }

        // NDEF stuff
        Ndef mNdef = Ndef.get(tag); // this is NDEF technology stuff

        if (mNdef == null) {
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(),
                        "mNdef is null",
                        Toast.LENGTH_SHORT).show();
            });
        }

        // Check that it is an Ndef capable card
        if (mNdef != null) {

            // If we want to read
            // As we did not turn on the NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK
            // We can get the cached Ndef message the system read for us.

            NdefMessage mNdefMessage = mNdef.getCachedNdefMessage();
            //ndefMessageString = mNdefMessage.toString();

            // Make a Sound
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(getApplicationContext(),
                        notification);
                r.play();
            } catch (Exception e) {
                // Some error playing sound
            }
            NdefRecord[] record = mNdefMessage.getRecords();
            String ndefContent = "";
            int ndefRecordsCount = record.length;
            ndefContent = "nr of records: " + ndefRecordsCount + "\n";
            // Success if got to here
            runOnUiThread(() -> {
                Toast.makeText(getApplicationContext(),
                        "Read from NFC success, number of records: " + ndefRecordsCount,
                        Toast.LENGTH_SHORT).show();
            });
        }
        // NDEF end
         */

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