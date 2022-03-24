package openSW;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class makeKeyword {

	private String input_file;
	private String output_file = "./index.xml";
	
	public makeKeyword(String file) {
		this.input_file = file;
	}
	
	public void convertXml() {
		File dir = new File(input_file);
		File files[] = dir.listFiles();
		int count = 0;
		int num=0;
		
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docbuilder = docFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = docbuilder.newDocument();
			doc.setXmlStandalone(true);
			org.w3c.dom.Element docs = doc.createElement("docs");
			doc.appendChild(docs);
			
			for(File origin : files) {
				if(origin.getName().contains("xml")) {
					FileInputStream file = new FileInputStream(origin);
					
					org.jsoup.nodes.Document document = Jsoup.parse(file, null, "", Parser.xmlParser());
					
					for (Element e : document.select("title")) {
						org.w3c.dom.Element id = doc.createElement("doc");
						docs.appendChild(id);
						
						id.setAttribute("id", Integer.toString(count));
						
						org.w3c.dom.Element title = doc.createElement("title");
						title.appendChild(doc.createTextNode(e.text()));
						id.appendChild(title);
						
						org.w3c.dom.Element body = doc.createElement("body");
						Element e1 = document.select("body").get(num);
						KeywordExtractor ke = new KeywordExtractor();
						KeywordList kl = ke.extractKeyword(e1.text(), true);
						String str = "";
						for (int i = 0; i < kl.size(); i++) {
							Keyword kwrd = kl.get(i);
							str += kwrd.getString() + ":" + kwrd.getCnt() + "#";
						}
						body.appendChild(doc.createTextNode(str));
						id.appendChild(body);
						count++;
						num++;
					}
				}
				num=0;
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
			
			System.out.println("END");
		} catch (Exception ee) {
			ee.printStackTrace();
		}
	}
}
