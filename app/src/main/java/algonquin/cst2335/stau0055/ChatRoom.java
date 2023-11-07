package algonquin.cst2335.stau0055;


import algonquin.cst2335.stau0055.data.ChatRoomModel;
import algonquin.cst2335.stau0055.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.stau0055.databinding.ReceiveMessageBinding;
import algonquin.cst2335.stau0055.databinding.SentMessageBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatRoom extends AppCompatActivity {

    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages;
    ChatRoomModel chatModel;
    private RecyclerView.Adapter myAdapter;
    ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name")
                .build();
        mDAO = db.cmDAO();


        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        chatModel = new ViewModelProvider(this).get(ChatRoomModel.class);

        messages = chatModel.messages.getValue();
        if (messages == null)
            chatModel.messages.postValue(messages = new ArrayList<ChatMessage>());

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() ->
        {
            messages.addAll(mDAO.getAllMessages());

            runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter));
        });

        setContentView(binding.getRoot());

        binding.sendButton.setOnClickListener(clk -> {

            String editTextArray = binding.editTextArray.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(editTextArray, currentDateandTime, true);
            messages.add(chatMessage);


            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() ->
            {
                chatMessage.id = (int) mDAO.insertMessage(chatMessage);

            });


            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.editTextArray.setText("");
        });

        binding.receiveButton.setOnClickListener(clk -> {
            String editTextArray = binding.editTextArray.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(editTextArray, currentDateandTime, false);
            messages.add(chatMessage);

            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() ->
            {
                chatMessage.id = (int) mDAO.insertMessage(chatMessage);

            });

            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.editTextArray.setText("");
        });


        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if (viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                } else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage chatMessage = messages.get(position);
                holder.messageText.setText(chatMessage.getMessage());
                holder.timeText.setText(chatMessage.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public int getItemViewType(int position) {

                ChatMessage message = messages.get(position);
                return message.isSentButton() ? 0 : 1;
            }

        });
    }


    class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;
        ImageView img;

        public MyRowHolder(@NonNull View theRootConstraintLayout) {
            super(theRootConstraintLayout);
            messageText = theRootConstraintLayout.findViewById(R.id.message);
            timeText = theRootConstraintLayout.findViewById(R.id.time);


            theRootConstraintLayout.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();

                if (position >= 0 && position < messages.size()) {
                    ChatMessage chatMessage = messages.get(position);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                    builder.setMessage("Do you want to delete this message: " + messageText.getText())
                            .setTitle("Question:")
                            .setNegativeButton("No", (dialog, cl) -> {
                            })
                            .setPositiveButton("Yes", (dialog, cl) -> {
                                messages.remove(position);
                                myAdapter.notifyItemRemoved(position);

                                Executor thread1 = Executors.newSingleThreadExecutor();
                                thread1.execute(() -> {
                                    mDAO.deleteMessage(chatMessage);
                                });

                                Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                        .setAction("undo", clk2 -> {
                                            // Add the removed message back to the database
                                            Executor thread2 = Executors.newSingleThreadExecutor();
                                            thread2.execute(() -> {
                                                mDAO.insertMessage(chatMessage);
                                            });
                                            messages.add(position, chatMessage);
                                            myAdapter.notifyItemInserted(position);
                                        }).show();
                            }).create().show();
                }
            });
        }
    }
}
