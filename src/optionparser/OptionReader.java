package optionparser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class OptionReader {
    protected Document doc;
    
    public OptionReader(String fileName) throws ParserConfigurationException, SAXException, IOException {
        File file = new File(fileName);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        this.doc = doc;
    }
    
    public ArrayList<Option> getOptions() {
        NodeList optionsList = doc.getElementsByTagName("option"); 
        for(int i = 0; i < optionsList.getLength(); i++) {
            
        }
        return null;
    }

}
