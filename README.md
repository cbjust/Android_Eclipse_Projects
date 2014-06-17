该项目包括以下工程：   
 
 
1.FragmentTest  
package:com.cb.test.fragment.*    
描述：Fragment的简单使用test  
  
2.Parser test  
package： com.cb.test  
描述：工程Test
(1) com.cb.test.web.ui   WebView的使用 Test
       
(2) com.cb.test.json.*     json 普通解析 / 使用Gson jar包 开发json解析的Test    
  
(3) com.cb.test.xml.parser.factory  使用HttpXmlFactoryBase 开发    
(4) com.cb.test.xml.parser.handler  Xml parser 的几种方法 Test， 会涉及到BaseXmlHandler类的使用   
  
3.Structure  
package: com.cb.structure.*  
描述：json/xml解析的基类   
  
一个问题记录：  
public abstract class HttpGsonFactoryBase<T> extends HttpFactoryBase<T>  
在该类中，实现了泛型方法:protected T AnalysisContent(String responseContent), 会导致抛出异常：java.lang.ClassCastException: com.google.gson.internal.LinkedTreeMap cannot be cast to com.cb.test.XX。  
具体原因:generic type T itself is not available during runtime. It's been erased. It's only available during compile time.  
解决方法是：在具体的子类中实现非泛型方法AnalysisContent, 如：protected ArrayList<Person> AnalysisContent(String responseContent)  
 
