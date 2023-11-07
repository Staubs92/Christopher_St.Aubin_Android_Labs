package algonquin.cst2335.stau0055;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name="id")
    public int id;

    @ColumnInfo(name="message")
    private String message;

    @ColumnInfo(name="TimeSent")
    private String timeSent;

    @ColumnInfo(name="SendOrReceive")
    private boolean isSentButton;

    public ChatMessage(String message, String timeSent, boolean isSentButton) {
        this.id = id;
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
