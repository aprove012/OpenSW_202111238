package scripts;

import java.io.IOException;

public class kuir {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		String command = args[0];
		String path = args[1];
		
		if(command.equals("-c")) {
			makeCollection collection = new makeCollection(path);
			collection.makeXml();
		}
		
		else if(command.equals("-k")) {
			makeKeyword keyword = new makeKeyword(path);
			keyword.convertXml();
		}
		
		else if(command.equals("-i")) {
			indexer index = new indexer(path);
			index.makePost();
		}
	}
}
