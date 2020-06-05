package Exercise.exercise1.RPC.Server;

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
import java.util.ArrayList;

public class AopXmlReader {
    public static void readXml(String filePath,String proxyname){
        String xml = load(filePath);
        try{
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            Document document;
            DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
            StringReader sr = new StringReader(xml);
            InputSource is = new InputSource(sr);
            document = dbBuilder.parse(is);
            Element root = document.getDocumentElement();
            findMethod(root.getChildNodes(),proxyname);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void findMethod(NodeList elementList,String proxyname) throws ClassNotFoundException {
        for (int i = 0; i < elementList.getLength(); i++) {
            Node elementNode = elementList.item(i);
            //System.out.println("allnode: "+elementNode.getNodeName());
            if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) elementNode;
                String name = element.getNodeName();
                if(name.equalsIgnoreCase("aop")){
                    readMethod(element.getChildNodes(),proxyname);
                }
                else{
                    findMethod(element.getChildNodes(),proxyname);
                }
            }
        }
    }

    public static void readMethod(NodeList elementList,String proxyname) throws ClassNotFoundException {
        String methodName = "";
        String proxy = "";
        String type = "";
        ArrayList<Class<?>> arg = new ArrayList<Class<?>>();
        for (int i = 0; i < elementList.getLength(); i++) {
            Node elementNode = elementList.item(i);
            if (elementNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) elementNode;
                String name = element.getNodeName();
                if("method".equals(name)){
                    if(methodName==null||methodName.length()==0) {
                        methodName = element.getFirstChild().getTextContent();
                    }
                }
                else if("type".equals(name)){
                    type = element.getFirstChild().getTextContent();
                }
                else if("proxy".equals(name)){
                    proxy = element.getFirstChild().getTextContent();
                }
                else if("methodarg".equals(name)){
                    //TODO 初始化class数组，获取反射
                    if (arg != null) {
                        arg.add(Class.forName(element.getFirstChild().getTextContent()));
                    }

                }

            }
        }
        if(proxy.equals(proxyname)){
            Class<?> [] para = new Class[arg.size()];
            for(int i = 0;i < arg.size();++i){
                para[i] = arg.get(i);
            }
            if(type.equals("before")){
                DynamicProxyFactory.beforeMethod = methodName;
                DynamicProxyFactory.beforeClass = para;
            }
            else if(type.equals("after")){
                DynamicProxyFactory.afterMethod = methodName;
                DynamicProxyFactory.afterClass = para;
            }
        }
    }

    public static String load(String path){
        try{
            File file = new File(path);
            FileReader reader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            String s = "";
            while ((s =bReader.readLine()) != null) {
                sb.append(s + "\n");
            }
            bReader.close();
            return sb.toString();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) {
//        AopXmlReader.readXml("src\\main\\java\\Exercise\\exercise1\\RPC\\Server\\Aop.xml","login");
//    }
}
