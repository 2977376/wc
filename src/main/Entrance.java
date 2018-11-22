package main;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.berkeley.BreadthCrawler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
public class Entrance extends BreadthCrawler{
	public StringBuffer sb=new StringBuffer();
	
	public Entrance(String crawlPath, boolean autoParse) {
	    super(crawlPath, autoParse);
	    this.addSeed("https://github.com/FIRHQ","taglist");//�б��ʼҳ��
	    this.addRegex("https://github.com/FIRHQ/*");
    }
	
	@Override
    public void visit(Page page, CrawlDatums next) {
	    Document doc = page.doc();
	    if(page.matchType("taglist")){//��ȡ�б�
	    	next.add(page.links("a[itemprop=name codeRepository]"),"tag");//Ŀ��ҳ��
	    	next.add(page.links("a[class=next_page]"),"taglist");//��һҳ�б�
	    }else if(page.matchType("tag")){//��ȡ��������
	    	Elements es=doc.select("a[class=social-count js-social-count]");
	    	Elements t=doc.select("strong[itemprop=name]>a");
	    	String text="title:"+t.html()+";Stars:"+es.first().html()+"\n";
	    	sb.append(text);
	    }
    }
 
    public static void main(String[] args) throws Exception {
		Entrance crawler = new Entrance("crawl", true);
	    crawler.setThreads(50);
	    crawler.setMaxExecuteCount(100);
	    crawler.start(3);
	    
	    //Ŀ����Ϣд���ļ�
	    String path="D:\\FIRHQ.txt";
	    if(args.length>0){//���Լ������ļ�·����·����Ҫ��ǰ��Ȩ
	    	path=args[0];
	    }
	    File file = new File(path);
	    if(!file.exists()){
	        file.getParentFile().mkdir();
	        file.createNewFile();
	    }else{
	    	FileWriter fileWriter =new FileWriter(file);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
	    }
    	OutputStreamWriter out = null;
    	BufferedWriter csvFileOutputStream = null;
    	out = new OutputStreamWriter(new FileOutputStream(file,true), "GBK");
    	csvFileOutputStream = new BufferedWriter(out, 1024);
    	csvFileOutputStream.write(crawler.sb.toString());
    	csvFileOutputStream.flush();
    	csvFileOutputStream.close();
    }
}
