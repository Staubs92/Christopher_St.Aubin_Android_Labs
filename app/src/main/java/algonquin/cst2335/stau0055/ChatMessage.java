package algonquin.cst2335.stau0055;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @ColumnInfo(name="message")
    public String message;
    @ColumnInfo(name="timeSent")
    public String timeSent;

    @ColumnInfo(name="isSentButton")
    private boolean isSentButton;

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo (name="id")
    public int id;

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSentButton = isSentButton;
    }

    public String getMessage(){
        return message;
    }

    public String getTimeSent(){
        return timeSent;
    }

    public boolean isSentButton(){
        return isSentButton;
    }



}
