package Exercise.exercise1.RPC.Client;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Vector;

public class HostXmlReader {
//    private static Vector<Host> hosts = null;
        // 返回xml中启动了的远程服务器
        public static Vector<Host>  readXml(String filePath){
            String xml = load(filePath);
            try{
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                Document document;
                DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
                StringReader sr = new StringReader(xml);
                InputSource is = new InputSource(sr);
                document = dbBuilder.parse(is);
                Element root = document.getDocumentElement();
                return findHost(root.getChildNodes());
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

        public  static Vector<Host> findHost(NodeList elementList){
            Vector<Host> hosts = new Vector<>();
            for (int i = 0; i < elementList.getLength(); i++) {
                Node elementNode = elementList.item(i);
                //System.out.println("allnode: "+elementNode.getNodeName());
                if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) elementNode;
                    String name = element.getNodeName();
                    if(name.equalsIgnoreCase("host")){
                        Host host = getHostInfo(element.getChildNodes());
                        if(host!=null)hosts.add(host);
                    }
//                    else{
//                        findHost(element.getChildNodes());
//                    }
                }
            }
            return hosts;
        }

        private  static Host getHostInfo(NodeList elementList){
            String methodName = "";
            String ip = "";
            int port  = 0;
            String state = "";
            for (int i = 0; i < elementList.getLength(); i++) {
                Node elementNode = elementList.item(i);
                if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) elementNode;
                    String name = element.getNodeName();
                    if(name.equals("ip")){
                        if(ip==null||ip.length()==0) {
                            ip = element.getFirstChild().getTextContent();
                        }
                    }
                    else if(name.equals("state")){
                        if(state==null||state.length()==0) {
                            state = element.getFirstChild().getTextContent();
                        }
                    }
                    else if(name.equals("port")){
                        if(port==0) {
                            port = Integer.parseInt(element.getFirstChild().getTextContent());
                        }
                    }
                }
            }
            if("active".equals(state)){
                return new Host(ip,port);
//                Client.activeHost.add(new Host(ip,port));
            }
            return null;
        }

        private  static String load(String path){
            try{
                File file = new File(path);
                FileReader reader = new FileReader(file);
                BufferedReader bReader = new BufferedReader(reader);
                StringBuilder sb = new StringBuilder();
                String s = "";
                while ((s =bReader.readLine()) != null) {
                    sb.append(s + "\n");
                    //System.out.println(s);
                }
                bReader.close();
                return sb.toString();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return null;
        }

}


