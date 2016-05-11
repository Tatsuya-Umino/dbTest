package controllers;

import play.*;
import play.mvc.*;
import views.html.*;
import static play.data.Form.*;
import play.data.*;
import play.api.mvc.Call;
import java.util.*;
import models.*;
import com.avaje.ebean.Query;

public class Application extends Controller {

    public static Result index() {
        List<Message> datas = Message.find.all();
        return ok(index.render("DBサンプル", datas));
    }

    public static Result add() {
    	Form<Message> form = form(Message.class);
        return ok(inputForm.render("新規メッセージ作成画面", "投稿フォーム", new Call("POST", "/create"), form));
    }

    public static Result setItem() {
    	Message m = new Message();
    	m.name = "hoge";
    	Form<Message> form = form(Message.class).fill(m);
    	List<Message> datas = new ArrayList<Message>();
        return ok(findForm.render("投稿メッセージID検索画面", "ID番号入力", new Call("POST", "/edit"), "id", "id", datas, form));
    }

    public static Result edit() {
    	Form<Message> createForm = form(Message.class).bindFromRequest();
        if (!createForm.hasErrors()) {
        	Message data = createForm.get();
        	Message m = Message.find.byId(data.id);
        	if(m == null){
        		data.name = "hoge";
        		Form<Message> form = form(Message.class).fill(data);
            	List<Message> datas = new ArrayList<Message>();
            	return badRequest(findForm.render("投稿メッセージID検索画面", "IDが存在しません", new Call("POST", "/edit"), "id", "id", datas, form));
        	}
        	Form<Message> form = form(Message.class).fill(m);
        	String msg = data.id + "の編集";
        	return ok(inputForm.render("投稿メッセージ編集画面", msg, new Call("POST", "/update"), form));
        } else {
        	List<Message> datas = new ArrayList <Message>();
            return badRequest(findForm.render("投稿メッセージID検索画面", "ERROR", new Call("POST", "/edit"), "id", "id", datas, createForm));
        }
    }

    public static Result delete() {
    	Message m = new Message();
    	m.name = "hoge";
    	Form<Message> form = form(Message.class);
    	List<Message> datas = new ArrayList <Message>();
        return ok(findForm.render("投稿メッセージID削除画面（指定したIDを削除する）", "削除するID番号", new Call("POST", "/destroy"), "id", "id", datas, form));
    }

    public static Result find() {
    	String name = Form.form().bindFromRequest().get("name");
        Query<Message> query = Message.find.where("name='" + name + "'");
        List<Message> datas =query.findList();
        Form<Message> form = form(Message.class);
        return ok(findForm.render("投稿者検索画面", "投稿の検索", new Call("GET", "/find"), "input", "name", datas, form));
    }

    public static Result create(){
    	Form<Message> createForm = form(Message.class).bindFromRequest();
        if (!createForm.hasErrors()) {
        	Message data = createForm.get();
        	java.util.Date d = new java.util.Date();
        	java.sql.Date d2 = new java.sql.Date(d.getTime());
        	data.postdate = d2;
          	data.save();
          return redirect("/");
        } else {
        	System.out.println(createForm.errors());;
            return badRequest(inputForm.render("新規メッセージ作成画面", "ERROR", new Call("POST", "/create"), createForm));
        }
    }

    public static Result update(){
    	Form<Message> createForm = form(Message.class).bindFromRequest();
        if (!createForm.hasErrors()) {
          Message data = createForm.get();
          java.util.Date d = new java.util.Date();
          java.sql.Date d2 = new java.sql.Date(d.getTime());
          data.postdate = d2;
          data.update();
          return redirect("/");
        } else {
        	return ok(inputForm.render("投稿メッセージ編集画面", "ERROR", new Call("POST", "/update"), createForm));
        }
    }

    public static Result destroy(){
    	Form<Message> createForm = form(Message.class).bindFromRequest();
        if (!createForm.hasErrors()) {
          Message data = createForm.get();
          Message m = Message.find.byId(data.id);
          if(m == null){
        	data.name = "hoge";
      		Form<Message> form = form(Message.class);
          	List<Message> datas = new ArrayList <Message>();
          	return ok(findForm.render("投稿メッセージID削除画面（指定したIDを削除する）", "IDが存在しません", new Call("POST", "/destroy"), "id", "id", datas, form));
      	}
          m.delete();
          return redirect("/");
        } else {
        	Message m = new Message();
        	m.name = "hoge";
        	createForm.fill(m);
        	List<Message> datas = new ArrayList <Message>();
            return ok(findForm.render("投稿メッセージID削除画面（指定したIDを削除する）", "ERROR", new Call("POST", "/destroy"), "id", "id", datas, createForm));
        }
    }
}

