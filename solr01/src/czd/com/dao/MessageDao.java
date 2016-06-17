package czd.com.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import czd.com.SolrContext;
import czd.com.model.Message;
import czd.com.model.Page;

public class MessageDao {
	public void addIndex(Message msg){
		try {
			SolrContext.getServer().addBean(msg);
			SolrContext.getServer().commit();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteIndex(String msg_id){
		try {
			SolrContext.getServer().deleteById(msg_id);
			SolrContext.getServer().commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void updateIndex(Message msg){
		try {
			SolrContext.getServer().deleteById(msg.getId());
			SolrContext.getServer().addBean(msg);
			SolrContext.getServer().commit();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public SolrDocumentList listIndex(String keyWord){
		SolrDocumentList sdl = null;
		try {
			SolrQuery query = new SolrQuery(keyWord);
			QueryResponse resp = SolrContext.getServer().query(query);
			 sdl= resp.getResults();
		} catch (SolrServerException e) {
			e.printStackTrace();
		}		
		return sdl;
	}
	
	//分页实现搜索
	public Page<Message> pageIndex(String keyWord,int pageNum,int rows){
		Page<Message> page = new Page<Message>();
		int startIndex = (int)Page.getStartOfPage(pageNum, rows);
		SolrDocumentList sdl = null;
		try {
			SolrQuery query = new SolrQuery(keyWord);
			//需要设置为高亮的域，标题和内容都设置为高亮
			query.setParam("hl.fl", "msg_title,msg_content");
			query.setHighlight(true).setHighlightSimplePre("<span class='highlighter'>").setHighlightSimplePost("</span>");
			query.setStart(startIndex).setRows(rows);
			QueryResponse resp = SolrContext.getServer().query(query);
			sdl= resp.getResults();
			Map<String,Map<String, List<String>>> highlighting = resp.getHighlighting();
			List<Message> list = new ArrayList<Message>();
			for(SolrDocument sd:sdl){
				Message msg = new Message();
				String id = (String)sd.getFieldValue("id");
				msg.setId(id);
				msg.setTitle((String)sd.getFieldValue("msg_title"));
				List<String> titles=highlighting.get(id).get("msg_title");
				if(titles!=null){
					msg.setTitle(titles.get(0));
				}
				
				//获取查询内容
				List<String> contents=highlighting.get(id).get("contents");
				if(contents!=null){
					String[] content = new String[contents.size()];
					for(int i=0;i<contents.size();i++){
						content[i] = contents.get(i);
					}
				}
				list.add(msg);
			}
			
			page.setResult(list);
			page.setTotalCount(sdl.getNumFound());
			page.setStart(startIndex);
			page.setPageSize(rows);
			
		} catch (SolrServerException e) {
			e.printStackTrace();
		}		
		return page;
	}

	
}
