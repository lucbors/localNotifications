package com.blogspot.lucbors.lnb.mobile.beans;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import java.util.Date;
import java.util.HashMap;

import oracle.adfmf.amx.event.ActionEvent;
import oracle.adfmf.framework.api.AdfmfContainerUtilities;
import oracle.adfmf.framework.api.AdfmfJavaUtilities;
import oracle.adfmf.framework.api.MafNativeLocalNotificationOptions;
import oracle.adfmf.framework.exception.AdfException;


public class LocalNotificationBean {
    private String title = "Reminder";
    private String alert = "Did you forget Something Today?";
    private Date date = null;
    private int badge = -1;
    private boolean sound;
    private boolean vibration;
    private String notificationId;

   

    public LocalNotificationBean() {
    }

    public void cancelLocalNotification(ActionEvent actionEvent) {   
        try {
       // We use the notificationId that was set when the notification was added
       //setNotificationId( AdfmfContainerUtilities.addLocalNotification(options));
          
          System.out.println("now cancelling " + notificationId);
          String cancelledNotificationId = AdfmfContainerUtilities.
                 cancelLocalNotification(notificationId);
          System.out.println("Notification " + cancelledNotificationId +" successfully canceled"); 
          
            Boolean recreate = (Boolean)AdfmfJavaUtilities.evaluateELExpression(
            "#{preferenceScope.application.ReCreateNotificationAfterCancel.ReCreate}");
            // if recreate preference is true
            if (recreate.booleanValue()){
               addNotification();
            }
        }
        catch(AdfException e) {
          System.err.println("There was a problem canceling notification");
        }
    }

    public void addLocalNotification(ActionEvent actionEvent) {
 
      addNotification();
    }
     
    public void addNotification(){
        String notificationDate = "now";
        try
        {
            // Set the notification options
            MafNativeLocalNotificationOptions options = new MafNativeLocalNotificationOptions();
            options.setTitle(title);
            options.setAlert(alert);
            if (date != null) {
                date.setSeconds(0); // Clear the seconds component to fire on the minute
                LocalDateTime l = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneOffset.UTC);
                options.setDate(l);
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                notificationDate = dateFormat.format(date);
            }
            options.setBadge(badge);
            if (sound) {
                options.setSound(MafNativeLocalNotificationOptions.SOUND_DEFAULT_SYSTEM);
            }
            if (vibration) {
                options.setVibration(MafNativeLocalNotificationOptions.VIBRATION_DEFAULT_SYSTEM);
            }
            // Set 3 values in the JSON payload
            HashMap<String,Object> payload = new HashMap<String, Object>();
            payload.put("key1", 1);
            payload.put("key2", "hello");
            payload.put("key3", true);
            options.setPayload(payload);
            
            options.setRepeat(MafNativeLocalNotificationOptions.RepeatInterval.MINUTELY);
            
            // Add the notification
            setNotificationId( AdfmfContainerUtilities.addLocalNotification(options));
            
            System.out.println("++++ Notification added successfully for " + notificationDate);
            System.out.println("++++ Notification ID is " + notificationId);
        }
        catch(Exception e)
        {
            System.out.println("++++ There was a problem adding notification: " + e.getMessage());
            e.printStackTrace();
        }    
    }


    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationId() {
        return notificationId;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getAlert() {
        return alert;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public int getBadge() {
        return badge;
    }

    public void setSound(boolean sound) {
        this.sound = sound;
    }

    public boolean getSound() {
        return sound;
    }

    public void setVibration(boolean vibration) {
        this.vibration = vibration;
    }

    public boolean getVibration() {
        return vibration;
    }
}
