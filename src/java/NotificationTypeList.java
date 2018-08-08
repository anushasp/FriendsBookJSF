/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rama
 */
public class NotificationTypeList 
{
    private String notif;
    private int type;
    
    public NotificationTypeList(String n, int t)
    {
        notif = n;
        type = t;
    }

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

   
}

    

