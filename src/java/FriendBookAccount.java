/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;


/**
 *
 * @author rama
 */

public class FriendBookAccount  {
    /**
     * Creates a new instance of FriendBookAccount
    */
    private String name;
    private String gender;
    private String id;
    private String psw;
    private String bday;
    private String school;
    private String msg;
    private String friendname;
    private List<String> friendsList;
    private String messg;
    private String friendrequestname;
    private String checkAccount = "";
    private String stat = "";
    private String postmsg = "";
    private String post;
    private List<String> hashtagslist;
    private String searchhashtag;
    private String hashtagresult="";
    private List<String> updateslist;
    private String commentupdate="";
    private String selected;
    private String updatecommentmsg="";
    private List<String> notificationlist;
    private int notifcount=0;
    private ArrayList<UpdateCommentList> items;
    private List<NotificationTypeList> notitems;
    private NotificationTypeList notifitem;
     private String replymessg;
     private boolean ifaccept=false;
    
    
    public FriendBookAccount(String n, String g, String i, String p, String b, String sc)
    {
        name = n;
        gender = g;
        id = i;
        psw = p;
        bday = b;
        school = sc;
    }
    
    public void welcome() throws IOException 
    {
        friendname="";
         items = new ArrayList<UpdateCommentList>();
        
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            int latest5, latest;
            //connect to the databse
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            //create statement
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * from updatenumber ");
            if (resultSet.next()) {
                latest = resultSet.getInt("updatenum");
                if (latest <= 5) {
                    latest5 = 1;
                } else {
                    latest5 = latest - 4;
                }
                int k = statement.executeUpdate("Delete from UpdateComment");
                statement = connection.createStatement();
                resultSet = statement.executeQuery("Select * from updates "
                        + "where updateid = '" + latest5 + "'");

                int Pid = 1;
                String updateby ="";
                String comment ="";
                while (resultSet.next()) 
                {
                    updateby= resultSet.getString(5)+" ----by "+resultSet.getString(2); 
                    comment=resultSet.getString(6);
                    items.add(new UpdateCommentList(updateby, comment));
                    
                   
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery("Select * from updates "
                            + "where updateid = '" + latest5 + "'");
                    if (resultSet.next()) 
                    {
                        int r = statement.executeUpdate("insert into UpdateComment values "
                                + "('" + Pid + "', '" + resultSet.getInt("updateid") + "',"
                                + "'" + resultSet.getString("userid") + "', '" + resultSet.getString("updatecontent") + "','" + resultSet.getString("comment") + "')");
                        Pid++;
                    }
                    latest5++;
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery("Select * from updates "
                            + "where updateid = '" + latest5 + "'");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     public List<String> DisplayUpdates() throws IOException {

        updateslist = new ArrayList<String>();
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            //create statement
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * from UpdateComment ");
            String updateby="";
            while (resultSet.next()) 
            {
                updateby=resultSet.getString(4);
                updateslist.add(updateby);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return updateslist;
    }
     public void CommentUpdatesPosts() throws IOException {
       
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            //create statement
            String comment = "";
            statement = connection.createStatement();
            
            resultSet = statement.executeQuery("Select * from UpdateComment where content = '" + selected + "'");
            if (resultSet.next()) 
            {
                comment = comment + resultSet.getString(5);
                commentupdate = commentupdate + "-----Posted by " + id  ;
                comment = comment + "<br/>"+ commentupdate;
                int r = statement.executeUpdate("Update updates set "
                        + "comment = '" + comment + "' where updatecontent = '" + selected + "'");
            }
            commentupdate="";
            updatecommentmsg ="Comment posted";
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
     
      public void DisplayNotfications() throws IOException 
      {
        
        notitems = new ArrayList<NotificationTypeList>();
        notifcount=0;
        notificationlist = new ArrayList<String>() ;
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            statement = connection.createStatement();
            int m = statement.executeUpdate("Delete from shownotifications");
            int y = 1;
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * from notifications "
                    + "where userid = '" + id + "'");
            int showid = 1;
            while (resultSet.next()) 
            {
                int l = statement.executeUpdate("insert into shownotifications values "
                        + "('" + showid + "', '" + resultSet.getInt(1) + "', '" + resultSet.getInt(4) + "','"
                        + resultSet.getString(3) + "')");
                showid++;
                statement = connection.createStatement();
                resultSet = statement.executeQuery("Select * from notifications "
                        + "where userid = '" + id + "' and notid > '" + y + "'");
                if (resultSet.next()) 
                {
                    y = resultSet.getInt(1);
                }
                statement = connection.createStatement();
                resultSet = statement.executeQuery("Select * from notifications "
                        + "where userid = '" + id + "' and notid > '" + y + "'");
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * from shownotifications ");
            while (resultSet.next()) 
            {
                notifcount++;
                notitems.add(new NotificationTypeList(resultSet.getString(4),resultSet.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
      
    public void AcceptFriendRequest()
    {
        
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * from notifications where content = '" + notifitem.getNotif() + "'");
            if (resultSet.next()) {
                int p = statement.executeUpdate("insert into friends values "
                        + "('" + resultSet.getString(5) + "','" + id + "')");
            }
            
            statement = connection.createStatement();
            int k = statement.executeUpdate("Delete from notifications where content = '" + notifitem.getNotif() + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        
    }
     public void RejectFriendRequest()
    {
       
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            statement = connection.createStatement();
            int k = statement.executeUpdate("Delete from notifications where content = '" + notifitem.getNotif() + "'");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     public void IgnoreMessage()
    {
       
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            statement = connection.createStatement();
            int k = statement.executeUpdate("Delete from notifications where content = '" + notifitem.getNotif() + "'");
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     public void Accept() 
     {
          ifaccept=true;
     }
    public void ReplyMessage() 
    {
        ifaccept=false;
        String senderid="";
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {

            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * from notifications where content = '" + notifitem.getNotif() + "'");
            if (resultSet.next()) 
            {
                senderid = resultSet.getString(5);
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * from message "
                    + "where id1 = '" + id + "' and id2 ='" + senderid + "'");
            int type = 1;
            if (resultSet.next()) 
            {
                String message = resultSet.getString(3);
                message = message + "</br>" + replymessg + "---- by" + id;
                int r = statement.executeUpdate("Update message set message = '" + message
                        + "' where id1 = '" + id + "' and id2 ='" + senderid + "'");
                int notid = 1;
                statement = connection.createStatement();
                resultSet = statement.executeQuery("Select * from notificationnumber");
                int nextnotid = 1;
                if (resultSet.next()) 
                {
                    notid = resultSet.getInt(1) + 1;
                    nextnotid = resultSet.getInt(1) + 1;
                }
                else 
                {
                    int k = statement.executeUpdate("insert into notificationnumber values "
                            + "('" + nextnotid + "')");
                }

                int n = statement.executeUpdate("update notificationnumber set nextnotid = '" + nextnotid + "'");
                int k = statement.executeUpdate("insert into notifications values "
                        + "('" + notid + "', '" + senderid + "', '" + replymessg + "', '" + type + "', '" + id + "')");
            }
            else
            {
               statement = connection.createStatement();
                resultSet = statement.executeQuery("Select * from message "
                        + "where id1 = '" + senderid + "' and id2 ='" + id + "'");

                if (resultSet.next()) 
                {
                    String message = resultSet.getString(3);
                    message = message + "</br>" + replymessg + "---- by" + id;
                    int r = statement.executeUpdate("Update message set message = '" + message
                            + "' where id1 = '" + senderid + "' and id2 ='" + id + "'");
                } 
                else 
                {
                    replymessg = replymessg + "---- by" + id;
                    int m = statement.executeUpdate("insert into message values "
                            + "('" + friendname + "', '" + id + "', '" + replymessg + "')");
                }
                int notid = 1;
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery("Select * from notificationnumber");
                    int nextnotid = 1;
                    if (resultSet.next()) {
                        notid = resultSet.getInt(1) + 1;
                        nextnotid = resultSet.getInt(1) + 1;
                    } else {
                        int k = statement.executeUpdate("insert into notificationnumber values "
                                + "('" + nextnotid + "')");
                    }

                    int n = statement.executeUpdate("update notificationnumber set nextnotid = '" + nextnotid + "'");
                    int k = statement.executeUpdate("insert into notifications values "
                            + "('" + notid + "', '" + senderid + "', '" + replymessg + "', '" + type + "', '" + id + "')");                    
            }
            statement = connection.createStatement();
            int k = statement.executeUpdate("Delete from notifications where content = '" + notifitem.getNotif() + "'");
           
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    
}
    
    public void CreatePost() {
        
        int postid = 1;
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            //connect to the databse
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            //create statement
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * from postnumber");
            int nextid = 1;
            if (resultSet.next()) 
            {
                postid = resultSet.getInt(1) + 1;
                nextid = resultSet.getInt(1) + 1;
            } 
            else 
            {
                int k = statement.executeUpdate("insert into postnumber values "
                        + "('" + nextid + "')");
            }

            int type = 2;

            int t = statement.executeUpdate("update postnumber set postnum = '" + nextid + "'");
            int r = statement.executeUpdate("insert into post values "
                    + "('" + postid + "', '" + id + "', '" + post + "')");
            postmsg= "Post Created";

            int updateid = 1;
            statement = connection.createStatement();
            //search the accountID in the onlineAccount table
            resultSet = statement.executeQuery("Select * from updatenumber");
            int nextupid = 1;

            if (resultSet.next()) {
                updateid = resultSet.getInt(1) + 1;
                nextupid = resultSet.getInt(1) + 1;

            } else {
                int k = statement.executeUpdate("insert into updatenumber values "
                        + "('" + nextupid + "')");
            }
            String comment = "";
            int n = statement.executeUpdate("update updatenumber set updatenum = '" + nextupid + "'");

            int l = statement.executeUpdate("insert into updates values "
                    + "('" + updateid + "', '" + id + "', '" + type + "','" + postid + "','" + post + "','"
                    + comment + "')");
            int pid = 0;
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * from post where post = '" + post + "'");
            if (resultSet.next()) 
            {
                pid = resultSet.getInt(1);
            }
            int count = 1;

            char start = post.charAt(0);
            if (start == '#') {
                int end = 0;
                boolean first = true;
                for (int j = 0; j < post.length() - 1; j = j + end) {
                    String hash = "#";
                    String hashpost = "";
                    char d = post.charAt(j);
                    if (d == '#') {
                        for (int i = j + 1, k = post.length(); i < k; i++) {
                            char c = post.charAt(i);
                            if (c != '#') {
                                hash = hash + c;
                            } else if (c == '#') {
                                end = i - j;
                                break;
                            }
                        }
                        statement = connection.createStatement();
                        resultSet = statement.executeQuery("Select * from hashtags where hash = '" + hash + "'");
                        if (resultSet.next()) {
                            int counter = resultSet.getInt("count") + 1;
                            String postids = pid + " , " + resultSet.getString("postid");
                            String posts = post + "\n" + resultSet.getString("posts");
                            posts = posts + "  ---- by" + id;
                            int z = statement.executeUpdate("update hashtags set count= '" + counter
                                    + "' , postid = '" + postids + "' ,userid = '" + id + "' ,"
                                    + " posts = '" + posts + "' where hash = '" + hash + "'");
                        } else {
                            if (first == true) {
                                hashpost = post + "  ---- by " + id;
                            } else {
                                hashpost = hashpost;
                            }
                            int o = statement.executeUpdate("insert into hashtags values "
                                    + "('" + hash + "', '" + count + "', '" + id + "','" + pid + "','" + hashpost + "')");
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
                
    public void UpdateProfile() {
       
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //connect to the databse
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            //create statement
            statement = connection.createStatement();
            //search the accountID in the onlineAccount table
            resultSet = statement.executeQuery("Select * from friendbookaccount "
                    + "where userID = '" + id + "'");
            
            if (resultSet.next()) 
            {
                    int t = statement.executeUpdate("Update friendbookaccount set "
                            + "name = '" + name+ "' where userID = '" + id + "'");
                   
                    int m = statement.executeUpdate("Update friendbookaccount set "
                            + "gender = '" + gender + "' where userID = '" + id + "'");
                   
                    int n = statement.executeUpdate("Update friendbookaccount set "
                            + "bday = '" + bday + "' where userID = '" + id + "'");
                   
              
                    int o = statement.executeUpdate("Update friendbookaccount set "
                            + "school = '" + school + "' where userID = '" + id + "'");
                   
                msg= "Profile Updated";
            }
            int updateid = 1;
            resultSet = statement.executeQuery("Select * from updatenumber");
            int nextupid = 1;
            int type = 1;
            int postid = 0;
            String ProfUpdate=id+" has updated his/her profile";
            if (resultSet.next()) {
                updateid = resultSet.getInt(1) + 1;
                nextupid = resultSet.getInt(1) + 1;

            } else {
                int k = statement.executeUpdate("insert into updatenumber values "
                        + "('" + nextupid + "')");
            }
            String comments = "";
            int n = statement.executeUpdate("update updatenumber set updatenum = '" + nextupid + "'");

            int l = statement.executeUpdate("insert into updates values "
                    + "('" + updateid + "', '" + id + "', '" + type + "','" + postid + "','" + ProfUpdate + "','"
                    + comments + "')");
            
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
             
            
        } 
        finally 
        {
            //close the database
            try 
            {
                connection.close();
                statement.close();
                resultSet.close();
            } 
        

            catch (Exception e) 
            {
                e.printStackTrace();
                 
            }
        }
    }  
    
     public List<String> Friends()
     {
        
        friendsList = new ArrayList<String>() ;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        
        try
        {
            
            final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
            
            //connect to the database with user name and password
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");  
            statement = connection.createStatement();
            
             resultSet = statement.executeQuery("Select * from friends "
                    + "where id1 = '" + id + "' || id2 = '" + id + "'");

            while (resultSet.next()) 
            {
                if (id.equals(resultSet.getString(1))) 
                {
                   friendsList.add(resultSet.getString(2));

                } 
                else if (id.equals(resultSet.getString(2))) 
                {
                   friendsList.add(resultSet.getString(1));
                }
            } 
            
        }
        catch (SQLException e)
        {           
            e.printStackTrace();
        }
        finally
        {
            try
            {
                resultSet.close();
                statement.close();
                connection.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();         
            }
        }
        return friendsList;
    }
     
     public String ViewFriendsProfile()
     {
         String stat="";
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {

            //connect to the databse
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            //create statement
            statement = connection.createStatement();
            //search the accountID in the onlineAccount table
           
            resultSet = statement.executeQuery("Select * from friendbookaccount "
                    + "where userID = '" + friendname + "'");
            if (resultSet.next()) 
            {
                
                stat=stat+"Name:"+ resultSet.getString(1)+"<br/>"+"Gender:" + resultSet.getString(2)+
                       "<br/>" + "School:" + resultSet.getString(6)+"<br/>" + " Bday:" + resultSet.getString(5);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally 
        {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return stat;
     }
     
     
    public String ViewPrevMessages()
    {
       
        String curmsg = "";
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //connect to the databse
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            statement = connection.createStatement();
            
            resultSet = statement.executeQuery("Select * from friends "
                    + "where (id1 = '" + friendname + "' and id2 = '" + id + "') or "
                    + "(id1 = '" + id + "' and id2 = '" + friendname + "')");
            int status = 0;
            Boolean friend = false;
            if (resultSet.next())
            {
                friend = true;
                statement = connection.createStatement();
                resultSet = statement.executeQuery("Select * from message "
                        + "where (id1 = '" + friendname + "' and id2 = '" + id + "') or "
                        + "(id1 = '" + id + "' and id2 = '" + friendname + "')");
                if (resultSet.next())
                {
                    curmsg = curmsg + resultSet.getString(3);
                }
            } 
            else 
            {
               if(friendname!=null && friendname!="")
                {
                curmsg = "Not a friend";
                }
            }
           
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            //close the database
            try
            {
                connection.close();
                statement.close();
                resultSet.close();
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
        return curmsg;
    }

    public void SendMessage() 
    {
       
        int type=1;
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            //connect to the databse
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery("Select * from message "
                            + "where id1 = '" + id + "' and id2 ='" + friendname + "'");
             if (resultSet.next())
             {
               
                String message = resultSet.getString(3);
                message = message + "</br>" + messg + "---- by " + id;
                int r = statement.executeUpdate("Update message set message = '" + message
                        + "' where id1 = '" + id + "' and id2 ='" + friendname + "'");
                 int notid = 1;
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery("Select * from notificationnumber");
                    int nextnotid = 1;
                    if (resultSet.next()) {
                        notid = resultSet.getInt(1) + 1;
                        nextnotid = resultSet.getInt(1) + 1;
                    } else {
                        int k = statement.executeUpdate("insert into notificationnumber values "
                                + "('" + nextnotid + "')");
                    }

                    int n = statement.executeUpdate("update notificationnumber set nextnotid = '" + nextnotid + "'");
                    int k = statement.executeUpdate("insert into notifications values "
                            + "('" + notid + "', '" + friendname + "', '" + messg + "', '" + type + "', '" + id + "')");
            } 
             else 
             {
                statement = connection.createStatement();
                resultSet = statement.executeQuery("Select * from message "
                        + "where id1 = '" + friendname + "' and id2 ='" + id + "'");

                if (resultSet.next()) 
                {
                    String message = resultSet.getString(3);
                    message = message + "</br>" + messg + "---- by " + id;
                    int r = statement.executeUpdate("Update message set message = '" + message
                            + "' where id1 = '" + friendname + "' and id2 ='" + id + "'");
                } 
                else 
                {
                    messg = messg + "---- by" + id;
                    int m = statement.executeUpdate("insert into message values "
                            + "('" + friendname + "', '" + id + "', '" + messg + "')");
                }
                int notid = 1;
                    statement = connection.createStatement();
                    resultSet = statement.executeQuery("Select * from notificationnumber");
                    int nextnotid = 1;
                    if (resultSet.next()) {
                        notid = resultSet.getInt(1) + 1;
                        nextnotid = resultSet.getInt(1) + 1;
                    } else {
                        int k = statement.executeUpdate("insert into notificationnumber values "
                                + "('" + nextnotid + "')");
                    }

                    int n = statement.executeUpdate("update notificationnumber set nextnotid = '" + nextnotid + "'");
                    int k = statement.executeUpdate("insert into notifications values "
                            + "('" + notid + "', '" + friendname + "', '" + messg + "', '" + type + "', '" + id + "')");   
                     messg="";

            }
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            try 
            {
                connection.close();
                statement.close();
                resultSet.close();
            } 
            catch (Exception e) 
            {
                e.printStackTrace();
            }
        }
    }
    
    public void CheckAccountExists() 
    {
        
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * from friendbookaccount "
                    + "where userID = '" + friendrequestname + "'");
            if (resultSet.next()) 
            {
                checkAccount= "Account found";
            }
            else 
            {
                if (friendrequestname != null) 
                {
                    checkAccount = "No Account found";
                }
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

     public void SendFriendRequest() {
       
        String notification="";
        int type = 0;
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

         try {
             connection = DriverManager.getConnection(DATABASE_URL,
                     "saripellaa6306", "1517381");

             statement = connection.createStatement();
             resultSet = statement.executeQuery("Select * from friends "
                     + "where (id1 = '" + friendrequestname + "' and id2 = '" + id + "') or "
                     + "(id1 = '" + id + "' and id2 = '" + friendrequestname + "')");
             if (resultSet.next()) 
             {
                 stat = "You both are already Friends";
             } 
             else 
             {
                 notification = notification + id + " has send you a FriendRequest" + "\n";
                 int notid = 1;
                 statement = connection.createStatement();
                 resultSet = statement.executeQuery("Select * from notificationnumber");
                 int nextnotid = 1;
                 if (resultSet.next()) 
                 {
                     notid = resultSet.getInt(1) + 1;
                     nextnotid = resultSet.getInt(1) + 1;
                 } 
                 else 
                 {
                     int k = statement.executeUpdate("insert into notificationnumber values "
                             + "('" + nextnotid + "')");
                 }
                 int n = statement.executeUpdate("update notificationnumber set nextnotid = '" + nextnotid + "'");

                 int t = statement.executeUpdate("insert into notifications values "
                         + "('" + notid + "', '" + friendrequestname + "', '" + notification + "', '" + type + "', '" + id + "')");
                 stat = "Friend Request Sent!";
             }
         }
        catch (SQLException e) 
        {
            e.printStackTrace();
        } finally {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         
    }
     
      public List<String> DisplayHashtags() {
           
        hashtagslist = new ArrayList<String>() ;
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            //create statement
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * from hashtags "
                    + "order by count desc limit 3");
            while (resultSet.next()) 
            {
                hashtagslist.add(resultSet.getString(1));
            }
        } catch (SQLException e) {
        } finally {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return hashtagslist;
    }
      
      
       public void SearchHashtag() {
        
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/saripellaa6306";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(DATABASE_URL,
                    "saripellaa6306", "1517381");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("Select * from hashtags "
                    + "where hash = '" + searchhashtag + "'");
            
            if (resultSet.next())
            {
               hashtagresult=resultSet.getString(5);
            }
            else
            {
                hashtagresult= "No post with " + searchhashtag + " as hashtag" ;
            }
        } catch (SQLException e) {
        } finally {
            //close the database
            try {
                connection.close();
                statement.close();
                resultSet.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       
    }


     
    public String signOut()
    {
        FacesContext.getCurrentInstance()
                .getExternalContext().invalidateSession();
        return "You are Logged out";
    } 

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getBday() {
        return bday;
    }

    public void setBday(String bday) {
        this.bday = bday;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<String> getFriendsList() {
        return friendsList;
    }

    public void setFriendsList(List<String> friendsList) {
        this.friendsList = friendsList;
    }

    public String getFriendname() {
        return friendname;
    }

    public void setFriendname(String friendname) {
        this.friendname = friendname;
    }

    public String getMessg() {
        return messg;
    }

    public void setMessg(String messg) {
        this.messg = messg;
    }

    public String getFriendrequestname() {
        return friendrequestname;
    }

    public void setFriendrequestname(String friendrequestname) {
        this.friendrequestname = friendrequestname;
    }

    public String getCheckAccount() {
        return checkAccount;
    }

    public void setCheckAccount(String checkAccount) {
        this.checkAccount = checkAccount;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public String getPostmsg() {
        return postmsg;
    }

    public void setPostmsg(String postmsg) {
        this.postmsg = postmsg;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public List<String> getHashtagslist() {
        return hashtagslist;
    }

    public void setHashtagslist(List<String> hashtagslist) {
        this.hashtagslist = hashtagslist;
    }

    public String getSearchhashtag() {
        return searchhashtag;
    }

    public void setSearchhashtag(String searchhashtag) {
        this.searchhashtag = searchhashtag;
    }
    public String getHashtagresult() {
        return hashtagresult;
    }

    public void setHashtagresult(String hashtagresult) {
        this.hashtagresult = hashtagresult;
    }

    public List<String> getUpdateslist() {
        return updateslist;
    }

    public void setUpdateslist(List<String> updateslist) {
        this.updateslist = updateslist;
    }

    public String getCommentupdate() {
        return commentupdate;
    }

    public void setCommentupdate(String commentupdate) {
        this.commentupdate = commentupdate;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

   

    public String getUpdatecommentmsg() {
        return updatecommentmsg;
    }

    public void setUpdatecommentmsg(String updatecommentmsg) {
        this.updatecommentmsg = updatecommentmsg;
    }

    public List<String> getNotificationlist() {
        return notificationlist;
    }

    public void setNotificationlist(List<String> notificationlist) {
        this.notificationlist = notificationlist;
    }

    public int getNotifcount() {
        return notifcount;
    }

    public void setNotifcount(int notifcount) {
        this.notifcount = notifcount;
    }

    public List<NotificationTypeList> getNotitems() {
        return notitems;
    }

    public void setNotitems(ArrayList<NotificationTypeList> notitems) {
        this.notitems = notitems;
    }

    public NotificationTypeList getNotifitem() {
        return notifitem;
    }

    public void setNotifitem(NotificationTypeList notifitem) {
        this.notifitem = notifitem;
    }

    
   
    public ArrayList<UpdateCommentList> getItems() {
        return items;
    }

    public void setItems(ArrayList<UpdateCommentList> items) {
        this.items = items;
    }

    public String getReplymessg() {
        return replymessg;
    }

    public void setReplymessg(String replymessg) {
        this.replymessg = replymessg;
    }

    public boolean isIfaccept() {
        return ifaccept;
    }

    public void setIfaccept(boolean ifaccept) {
        this.ifaccept = ifaccept;
    }

    

}
