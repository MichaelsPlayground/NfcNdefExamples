# NFC NDEF Examples

This app is reading and writing NDEF formatted NFC tags.




For parsing: https://github.com/skjolber/ndef-tools-for-android



AndroidManifest.xml add permissions:
```plaintext
    <uses-permission android:name="android.permission.NFC" />
    <uses-permission android:name="android.permission.VIBRATE" />
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

