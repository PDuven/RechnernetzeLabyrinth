package networking;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import jaxb.MazeCom;
import tools.Debug;
import tools.DebugLevel;

public class XmlInStream extends UTFInputStream {

	private Unmarshaller unmarshaller;

	public XmlInStream(InputStream in) {
		super(in);
		try {
			JAXBContext jc = JAXBContext.newInstance(MazeCom.class);
			this.unmarshaller = jc.createUnmarshaller();
		} catch (JAXBException e) {
			Debug.print(Messages
					.getString("XmlInStream.errorInitialisingJAXBComponent"), //$NON-NLS-1$
					DebugLevel.DEFAULT);
		}
	}

	/**
	 * Liest eine Nachricht und gibt die entsprechende Instanz zurueck
	 * 
	 * @return
	 * @throws IOException
	 */
	public MazeCom readMazeCom() throws IOException {
		byte[] bytes = null;
		MazeCom result = null;
		try {
			String xml = this.readUTF8();
			Debug.print(
					Messages.getString("XmlInStream.received"), DebugLevel.DEBUG); //$NON-NLS-1$
			Debug.print(xml, DebugLevel.DEBUG);
			bytes = xml.getBytes();
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes);

			result = (MazeCom) this.unmarshaller.unmarshal(bais);
		} catch (JAXBException e) {
			e.printStackTrace();
			Debug.print(Messages.getString("XmlInStream.errorUnmarshalling"), //$NON-NLS-1$
					DebugLevel.DEFAULT);
			// } catch (IOException e1) {
			// // weiterleiten der Exception => damit Spieler korrekt entfernt
			// wird
			// if (e1 instanceof SocketException)
			// throw new SocketException();
			// // XXX: WICHTIG!
			// if (e1 instanceof EOFException)
			// throw new EOFException();
			// e1.printStackTrace();
			//			Debug.print(Messages.getString("XmlInStream.errorReadingMessage"), //$NON-NLS-1$
			// DebugLevel.DEFAULT);
		} catch (NullPointerException e) {
			Debug.print(
					Messages.getString("XmlInStream.nullpointerWhileReading"), //$NON-NLS-1$
					DebugLevel.DEFAULT);
		}
		return result;
	}

}