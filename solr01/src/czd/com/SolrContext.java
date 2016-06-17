package czd.com;

import java.net.MalformedURLException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;


public class SolrContext {
	private SolrContext(){}
	private final static String URL="http://localhost:8080/solr";
	private static CommonsHttpSolrServer server = null;
	
	/*
	 * 静态代码块，只会在第一次被调用的时候执行，后面不再执行
	 */
	static{
		try {
			System.out.println("正在连接服务器");
			server = new CommonsHttpSolrServer(URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public static SolrServer getServer(){
		return server;
	}
	
}
