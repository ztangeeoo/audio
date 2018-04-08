package list.util;

import com.thoughtworks.xstream.XStream;
import list.dto.TestMessageDTO;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/4/2.
 */
public class ConvertUtil {
    public static Map<String,String> xmlToMap(HttpServletRequest request)  {

        HashMap<String, String> map = new HashMap<>();
        SAXReader reader = new SAXReader();

        Document doc = null;
        try {
            ServletInputStream is = request.getInputStream();
            doc = reader.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        if(doc != null){
            Element root = doc.getRootElement();
            if(root!=null){
                List<Element> list = root.elements();
                for(Element e: list){
                    map.put(e.getName(),e.getText());
                }
            }
        }
        return map;
    }
    public static String objectToXml(Object message){
        XStream xs = new XStream();
        xs.alias("xml",message.getClass());
        return xs.toXML(message);
    }
}
