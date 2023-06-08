package com.example.jaadas2.util;


import org.shaechi.jaadas2.util.XMLToResult;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class XMLParseTest {

    public static void main(String []args) throws XMLStreamException, IOException {
        XMLToResult.parseXMLwithReader("E:\\code\\jaadas2\\soot-infoflow-summaries\\outtest.xml", 1);
    }

    public void testParseXML1()
    {

    }
}
