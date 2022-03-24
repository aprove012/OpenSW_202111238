package scripts;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.snu.ids.kkma.index.Keyword;
import org.snu.ids.kkma.index.KeywordExtractor;
import org.snu.ids.kkma.index.KeywordList;

public class indexer {
	
	private String input_file;
	private String output_file = "./index.post";
	
	
	public indexer(String file) {
		this.input_file = file;
	}
	
	@SuppressWarnings({"rawtypes", "unchecked", "nls"})
	public void makePost() throws IOException, ClassNotFoundException {
		int num = 0;
		int docNum = 0;
		File file = new File(input_file);
		FileInputStream origin = new FileInputStream(file);
		FileOutputStream fileStream = new FileOutputStream(output_file);
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileStream);

		org.jsoup.nodes.Document document = Jsoup.parse(origin, null, "", Parser.xmlParser());

		//전체 문서 수
		for(Element p : document.select("doc")) {
			docNum++;
		}
		
		HashMap xmlMap = new HashMap();
		
		
		Elements e = document.select("body");
		String fullStr = e.text().replaceAll(" ", "");
		String[] fullArr = fullStr.split("#|:");
		List<String> fullList = new ArrayList<>(Arrays.asList(fullArr));
		
		for(Element p : document.select("body")) {
			String value = "";
			String str = p.text();
			String[] arr = str.split("#|:");
			for(int i = 0; i < arr.length; i = i + 2) {
				double count = Collections.frequency(fullList, arr[i]);
				for(int j = 0; j<docNum; j++) {
					String find = e.get(j).text();
					String findArr[] = find.split("#|:");
					List<String> list = new ArrayList<>(Arrays.asList(findArr));
					if(list.indexOf(arr[i]) != -1) {
						value += j + " " + String.format("%.1f", Integer.parseInt(arr[i+1])*Math.log(docNum/count)) + " ";
					}
					else {
						value += j + " " + 0.0 + " ";
					}
				}
				xmlMap.put(arr[i], value);
				value = "";
			}
			num++;
		}
		
		objectOutputStream.writeObject(xmlMap);
		
		objectOutputStream.close();
		
		FileInputStream postStream = new FileInputStream(output_file);
		ObjectInputStream objectInputStream = new ObjectInputStream(postStream);
		
		Object object = objectInputStream.readObject();
		objectInputStream.close();
		
		System.out.println("읽어온 객체의 type → " + object.getClass());
		
		HashMap hashMap = (HashMap)object;
		Iterator<String> it = hashMap.keySet().iterator();
		
		while(it.hasNext()) {
			String key = it.next();
			String value = (String)hashMap.get(key);
			System.out.println(key + " → " + value);
		}
	}
}
