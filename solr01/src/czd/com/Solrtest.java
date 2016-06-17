package czd.com;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Before;
import org.junit.Test;

import czd.com.model.Message;

public class Solrtest {
	
	private final static String URL="http://localhost:8080/solr";
	private static 	CommonsHttpSolrServer server=null;
	
	@Before
	public void initServer(){
		try {
			server=new CommonsHttpSolrServer(URL);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test01(){
		try {			
			SolrInputDocument doc = new SolrInputDocument();
			//id是唯一的主键，最后一次添加的时候，同样的id的索引会覆盖掉之前的索引，相当于更新
			doc.addField("id", "1");
			doc.addField("msg_title", "杰佛森的采访聊乐福");
			doc.addField("msg_content", "杰佛森说的确不公平，因为詹姆斯和欧文手里有球，但乐福手里没有，所以不能决定自己什么时候投篮得分。");
			doc.addField("text", "测试默认域，乐福");
			server.add(doc);
			server.commit();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test02(){
		try {
			List<SolrInputDocument> docs = new ArrayList<SolrInputDocument>();
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", "2");
			doc.addField("msg_title", "总决赛第五场");
			doc.addField("msg_content", "库里能获得今年的MVP吗？");
			docs.add(doc);
			SolrInputDocument doc2 = new SolrInputDocument();
			doc2.addField("id", "3");
			doc2.addField("msg_title", "总决赛第六场");
			doc2.addField("msg_content", "你觉得谁能获得今年的总冠军，骑士队还是勇士队");			
			
			docs.add(doc);
			docs.add(doc2);
			
			//基于列表进行添加
			server.add(docs);
			server.commit();
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void test03(){
		try {
			//基于javabean来添加索引
			List<Message> msgs = new ArrayList<Message>();
			msgs.add(new Message("4", "通过实体bean来添加索引", new String[]{"能不能成功呢","应该能成功吧"}));
			msgs.add(new Message("5", "通过javabean来添加索引", new String[]{"到底能不能成功呢","应该吧，加油"}));
			server.addBeans(msgs);
			server.commit();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void test04(){		
		try {
			//基于document的查询
//			SolrQuery query = new SolrQuery("msg_title:乐福");
//			SolrQuery query = new SolrQuery("通过");
			SolrQuery query = new SolrQuery("*");
			//分页处理
			query.setStart(0).setRows(3);
			QueryResponse resp = server.query(query);			
			SolrDocumentList sdl = resp.getResults();
			//获得总数
			System.out.println(sdl.getNumFound());
			for(SolrDocument doc:sdl){				
//				System.out.println(doc);
				System.out.println(doc.get("msg_title"));
				System.out.println(doc.getFieldValue("msg_content"));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		
		
	}
	
	/*
	 * 基于javabean的查询，但不是很常用
	 * 缺点：
	 * 1、没办法获取记录的总数
	 * 2、没办法加上高亮字段
	 */
	@Test
	public void test05(){		
		try {
			//基于javaBean的查询
			SolrQuery query = new SolrQuery("*");
			QueryResponse resp = server.query(query);			
			List<Message> msgs = resp.getBeans(Message.class);
			for(Message msg:msgs){
				System.out.println(msg.getContent());
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		
		
	}
	
	@Test
	public void test06(){		
		try {
			//基于document的查询
			SolrQuery query = new SolrQuery("杰佛森");
			//设置高亮的前缀和后缀
			query.setHighlight(true).setHighlightSimplePre("<span class='highlighter'>").setHighlightSimplePost("</span>");
			//设置高亮的域，标题和内容都设置为高亮
			query.setParam("hl.fl", "msg_title,msg_content");
			QueryResponse resp = server.query(query);			
			SolrDocumentList sdl = resp.getResults();
			//获得总数
			System.out.println(sdl.getNumFound());
			Map<String,Map<String, List<String>>> highlighting=resp.getHighlighting();
			for(SolrDocument sd:sdl){
				String id=(String) sd.get("id");
				Map<String, List<String>> map = highlighting.get(id);
				System.out.print(map.get("msg_title"));
				System.out.println(map.get("msg_content"));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}		
		
	}
	
	@Test
	public void test07(){		
		try {
			
			SolrQuery query = new SolrQuery("*");
			//分页处理
			query.setStart(0).setRows(3);
			QueryResponse resp = SolrContext.getServer().query(query);			
			SolrDocumentList sdl = resp.getResults();
			//获得总数
			System.out.println(sdl.getNumFound());
			for(SolrDocument doc:sdl){				
//				System.out.println(doc);
				System.out.println(doc.get("msg_title"));
				System.out.println(doc.getFieldValue("msg_content"));
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	
	

}
