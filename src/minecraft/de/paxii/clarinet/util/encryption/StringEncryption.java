package de.paxii.clarinet.util.encryption;

import de.paxii.clarinet.util.settings.ClientSettings;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class StringEncryption {
	private String encryptionKey;
	private final String DEFAULT_ENCODING = "UTF-8";
	private BASE64Encoder enc = new BASE64Encoder();
	private BASE64Decoder dec = new BASE64Decoder();

	public StringEncryption(String encryptionKey) {
		this.encryptionKey = encryptionKey;
	}

	public StringEncryption() {
	}

	public String encryptString(String encryptionText) {
		if (this.encryptionKey == null) {
			this.encryptionKey = ClientSettings.getValue("client.enckey", String.class);
		}

		encryptionText = this.xorMessage(encryptionText, this.encryptionKey);

		return this.base64encode(encryptionText);
	}

	public String decryptString(String decryptionText) {
		if (this.encryptionKey == null) {
			this.encryptionKey = ClientSettings.getValue("client.enckey", String.class);
		}

		decryptionText = this.base64decode(decryptionText);
		return this.xorMessage(decryptionText, this.encryptionKey);
	}

	private String base64encode(String text) {
		try {
			String rez = enc.encode(text.getBytes(DEFAULT_ENCODING));
			return rez;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	private String base64decode(String text) {
		try {
			return new String(dec.decodeBuffer(text), DEFAULT_ENCODING);
		} catch (IOException e) {
			return null;
		}
	}

	private String xorMessage(String message, String key) {
		try {
			if (message == null || key == null) {
				return null;
			}

			char[] keys = key.toCharArray();
			char[] mesg = message.toCharArray();
			int ml = mesg.length;
			int kl = keys.length;
			char[] newmsg = new char[ml];

			for (int i = 0; i < ml; i++) {
				newmsg[i] = (char) (mesg[i] ^ keys[i % kl]);
			}

			mesg = null;
			keys = null;
			return new String(newmsg);
		} catch (Exception e) {
			return null;
		}
	}
}
