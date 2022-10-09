# NFC NDEF Examples

This app is reading and writing NDEF formatted NFC tags.

These NDEF types are implemented:
```plaintext
01 well known type - text
02 well known type - URI
03 well known type - Google streetview (Uri)
04 well known type - Email (Uri)
05 well known type - Coordinate (Uri)
06 well known type - Coordinate with user info (Uri)
07 well known type - Telefone number (Uri)
08 well known type - Address (Uri)
09 well known type - Google navigation (Uri)
10 well known type - Application (Uri)
11 well known type - Target address

```


For parsing: https://github.com/skjolber/ndef-tools-for-android

NDEFRecord: http://www.java2s.com/example/java-src/pkg/android/nfc/ndefrecord-c8783.html

Masterarbeit: https://diglib.tugraz.at/download.php?id=576a788c1cf1a&location=browse

```plaintext
Entwurf und Implementierung einer NFC-Anwendung fu ̈r den Einsatz im eCommerce-Bereich
```

AndroidManifest.xml add permissions:
```plaintext
    <uses-permission android:name="android.permission.NFC" />
    <uses-permission android:name="android.permission.VIBRATE" />
```

Email:
```plaintext
I/System.out: nr of records: 1
I/System.out: 
I/System.out: rec: 0 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 0 inf: 1 type: 55 payload: 06616e64726f696463727970746f40676d782e64653f7375626a6563743d546573746d61696c25323066726f6d2532304e464325323074616726626f64793d54686973253230697325323061253230746573746d61696c25323066726f6d253230612532304e4643253230746167 
I/System.out: androidcrypto@gmx.de?subject=Testmail%20from%20NFC%20tag&body=This%20is%20a%20testmail%20from%20a%20NFC%20tag 
I/System.out: Well known Uri XX payload
I/System.out: androidcrypto@gmx.de?subject=Testmail%20from%20NFC%20tag&body=This%20is%20a%20testmail%20from%20a%20NFC%20tag 
I/System.out: mailto:androidcrypto@gmx.de?subject=Testmail%20from%20NFC%20tag&body=This%20is%20a%20testmail%20from%20a%20NFC%20tag
```

Streetview
```plaintext
I/System.out: nr of records: 1
I/System.out: 
I/System.out: rec: 0 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 0 inf: 1 type: 55 payload: 00676f6f676c652e737472656574766965773a63626c6c3d33342e3739323334352c2d3131312e373632353331 
I/System.out: ��google.streetview:cbll=34.792345,-111.762531 
```

Link:
```plaintext
I/System.out: nr of records: 1
I/System.out: 
I/System.out: rec: 0 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 0 inf: 1 type: 55 payload: 046c696e6b736c6973742e6170702f3438356b4f774f 
I/System.out: linkslist.app/485kOwO 
```

WLAN-Verbindung
```plaintext
I/System.out: nr of records: 1
I/System.out: 
I/System.out: rec: 0 TNF: 2 (2 TNF_MIME_MEDIA)
I/System.out: rec 0 inf: 2 type: 6170706c69636174696f6e2f766e642e7766612e777363 payload: 100e00351026000101104500094b696464795f4e6574100300020020100f0002000c102700094c656f6e5f3230313810200006ffffffffffff 
I/System.out: ��5&��E��	Kiddy_Net���� ����'��	Leon_2018 �������� 
type: application/vnd.wfa.wsc
```

WLAN-Test
```plaintext
Schlüssel WPA2 Personal, AES/TKIP Verschlüsselung, SSID T_Wpa2_Aestkip, Passwort 12345678
I/System.out: nr of records: 1
I/System.out: 
I/System.out: rec: 0 TNF: 2 (2 TNF_MIME_MEDIA)
I/System.out: rec 0 inf: 2 type: 6170706c69636174696f6e2f766e642e7766612e777363 payload: 100e003910260001011045000e545f577061325f416573746b6970100300020020100f0002000c10270008313233343536373810200006ffffffffffff 
I/System.out: ��9&��E��T_Wpa2_Aestkip���� ����'�12345678 �������� 
I/System.out: TNF Mime Media XX payload
I/System.out: ��9&��E��T_Wpa2_Aestkip���� ����'�12345678 �������� 
I/System.out: TNF Mime Media XX type
I/System.out: application/vnd.wfa.wsc 

```

Ort:
```plaintext
I/System.out: nr of records: 1
I/System.out: 
I/System.out: rec: 0 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 0 inf: 1 type: 55 payload: 0067656f3a33392e35303335393735363433323830392c2d302e33323839343737383939363730363031 
I/System.out: ��geo:39.50359756432809,-0.3289477899670601 
I/System.out: Well known Uri XX payload
I/System.out: ��geo:39.50359756432809,-0.3289477899670601 
I/System.out: geo:39.50359756432809,-0.3289477899670601
```

benutzerdefinierter Ort:
```plaintext
I/System.out: nr of records: 1
I/System.out: 
I/System.out: rec: 0 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 0 inf: 1 type: 55 payload: 0067656f3a302c303f713d33362e31343434393838303432303336352c2d352e3335353436383136383835343731332847696272616c74617229 
I/System.out: ��geo:0,0?q=36.14449880420365,-5.355468168854713(Gibraltar) 
I/System.out: Well known Uri XX payload
I/System.out: ��geo:0,0?q=36.14449880420365,-5.355468168854713(Gibraltar) 
I/System.out: geo:0,0?q=36.14449880420365,-5.355468168854713(Gibraltar)
```

Address:
```plaintext
I/System.out: nr of records: 1
I/System.out: 
I/System.out: rec: 0 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 0 inf: 1 type: 55 payload: 0067656f3a302c303f713d53656c6d612b5374722e2b352532432b34353132372b457373656e 
I/System.out: ��geo:0,0?q=Selma+Str.+5%2C+45127+Essen 
I/System.out: Well known Uri XX payload
I/System.out: ��geo:0,0?q=Selma+Str.+5%2C+45127+Essen 
I/System.out: geo:0,0?q=Selma+Str.+5%2C+45127+Essen
```

Google navigation:
```plaintext
I/System.out: nr of records: 1
I/System.out: 
I/System.out: rec: 0 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 0 inf: 1 type: 55 payload: 00676f6f676c652e6e617669676174696f6e3a713d53656c6d612b5374722e2b352532432b34353132372b457373656e 
I/System.out: ��google.navigation:q=Selma+Str.+5%2C+45127+Essen 
I/System.out: Well known Uri XX payload
I/System.out: ��google.navigation:q=Selma+Str.+5%2C+45127+Essen 
I/System.out: google.navigation:q=Selma+Str.+5%2C+45127+Essen
```

Emergency information:
```plaintext
I/System.out: nr of records: 8
I/System.out: 
I/System.out: rec: 0 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 0 inf: 1 type: 54 payload: 02656e4e616d653a204a6f686e204d63436c616e65 
I/System.out: enName: John McClane 
I/System.out: 
I/System.out: rec: 1 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 1 inf: 1 type: 54 payload: 02656e42697274683a2031352e31302e3230 
I/System.out: enBirth: 15.10.20 
I/System.out: 
I/System.out: rec: 2 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 2 inf: 1 type: 54 payload: 02656e416464726573733a204e616b61746f6d6920506c617a612c204c2e412e 
I/System.out: enAddress: Nakatomi Plaza, L.A. 
I/System.out: 
I/System.out: rec: 3 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 3 inf: 1 type: 54 payload: 02656e416c6c6572676965732c206d656469636174696f6e732c20636f6e646974696f6e733a20616c6c657267696373 
I/System.out: enAllergies, medications, conditions: allergics 
I/System.out: 
I/System.out: rec: 4 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 4 inf: 1 type: 54 payload: 02656e426c6f6f6420547970653a204f2b 
I/System.out: enBlood Type: O+ 
I/System.out: 
I/System.out: rec: 5 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 5 inf: 1 type: 54 payload: 02656e4f7267616e20646f6e6f723a204e6f 
I/System.out: enOrgan donor: No 
I/System.out: 
I/System.out: rec: 6 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 6 inf: 1 type: 54 payload: 02656e4164646974696f6e616c3a205969707069 
I/System.out: enAdditional: Yippi 
I/System.out: 
I/System.out: rec: 7 TNF: 1 (1 TNF_WELL_KNOWN)
I/System.out: rec 7 inf: 1 type: 54 payload: 02656e456d657267656e637920636f6e746163743a20486f6c6c79776f6f64204d63436c616e652028333134353637383931323329 
I/System.out: enEmergency contact: Hollywood McClane (31456789123) 
I/System.out: Well known Text XX payload
I/System.out: enEmergency contact: Hollywood McClane (31456789123) 
I/System.out: Emergency contact: Hollywood McClane (31456789123)
```

Application:
```plaintext
I/System.out: nr of records: 1
I/System.out: 
I/System.out: rec: 0 TNF: 4 (4 TNF_EXTERNAL_TYPE)
I/System.out: rec 0 inf: 4 type: 616e64726f69642e636f6d3a706b67 payload: 636f6d2e696e6b77697265642e64726f6964696e666f 
I/System.out: com.inkwired.droidinfo 
I/System.out: TNF External type XX payload
I/System.out: com.inkwired.droidinfo 
I/System.out: TNF External type XX type
I/System.out: android.com:pkg 
```

https://www.oreilly.com/library/view/beginning-nfc/9781449324094/apa.html

```plaintext
Appendix A. NFC Specification Codes
There are a number of NFC Specification codes that you might use frequently. They’re all available in the NFC Specification on the NFC Forum site, but some of them are reproduced here for handy reference.

The NFC Forum Specification list provides handy summary definitions of all the NFC specifications. When you find yourself struggling to remember what acronyms such as LLCP, SNEP, TNF, and RTD mean, go to this page for the quick reference.

Table A-1. Type name formats
Type Name Format	Value
Empty

0x00

Well-Known type [NFC RTD]

0x01

MIME media-type [RFC 2046]

0x02

Absolute URI [RFC 3986]

0x03

External type [NFC RTD]

0x04

Unknown

0x05

Unchanged

0x06

Reserved

0x07

Table A-2. Common record type definitions
Record Type	RTD code
Text

T

URI

U

Smart Poster

Sp

Alternative Carrier

ac

Handover Carrier

Hc

Handover Request

Hr

Handover Select

Hs

Table A-3. URI identifier codes
Decimal	Hex	Protocol
0

0x00

None. The URI is added exactly as written.

1

0x01

http://www.

2

0x02

https://www.

3

0x03

http://

4

0x04

https://

5

0x05

tel:

6

0x06

mailto:

7

0x07

ftp://anonymous:anonymous@

8

0x08

ftp://ftp.

9

0x09

ftps:/

10

0x0A

sftp://

11

0x0B

smb://

12

0x0C

nfs://

13

0x0D

ftp://

14

0x0E

dav://

15

0x0F

news:

16

0x10

telnet://

17

0x11

imap:

18

0x12

rtsp://

19

0x13

urn:

20

0x14

pop:

21

0x15

sip:

22

0x16

sips:

23

0x17

tftp:

24

0x18

btspp://

25

0x19

btl2cap://

26

0x1A

btgoep://

27

0x1B

tcpobex://

28

0x1C

irdaobex://

29

0x1D

file://

30

0x1E

urn:epc:id:

31

0x1F

urn:epc:tag:

32

0x20

urn:epc:pat:

33

0x21

urn:epc:raw:

34

0x22

urn:epc:

35

0x23

urn:nfc:

36…255

0x24.,0xFF

Reserved for future use. (URI will be saved exactly ...
```

