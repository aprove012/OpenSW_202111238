package openSW;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class makeCollection {

	private String data_path;
	private String output_file = "./collection.xml";
	
	public makeCollection(String path) {
		this.data_path = path;
	}
	
	public void makeXml() {
		File dir = new File(data_path);
		File files[] = dir.listFiles();
		int count = 0;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docbuilder = docFactory.newDocumentBuilder();
			Document doc = docbuilder.newDocument();
			doc.setXmlStandalone(true);
			
			Element docs = doc.createElement("docs");
			doc.appendChild(docs);
			
			for(File file : files) {
				org.jsoup.nodes.Document html = Jsoup.parse(file, "UTF-8");
				String titleData = html.title();
				String bodyData = html.body().text();
				Element id = doc.createElement("doc");
				docs.appendChild(id);
				
				id.setAttribute("id", Integer.toString(count));
				
				Element title = doc.createElement("title");
				title.appendChild(doc.createTextNode(titleData));
				id.appendChild(title);
				
				Element body = doc.createElement("body");
				body.appendChild(doc.createTextNode(bodyData));
				id.appendChild(body);
				
				count++;
			}
				TransformerFactory transformerFactory = TransformerFactory.newInstance();
				
				Transformer transformer = transformerFactory.newTransformer();
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","4");
				transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
				
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(
						new FileOutputStream(new File(output_file)));
				transformer.transform(source, result);
				
				System.out.println("=========END=========");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
