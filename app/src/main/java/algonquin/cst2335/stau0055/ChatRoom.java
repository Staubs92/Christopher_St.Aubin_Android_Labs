package algonquin.cst2335.stau0055;


import algonquin.cst2335.stau0055.data.ChatRoomModel;
import algonquin.cst2335.stau0055.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.stau0055.databinding.ReceiveMessageBinding;
import algonquin.cst2335.stau0055.databinding.SentMessageBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatRoom extends AppCompatActivity {


    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages;
    ChatRoomModel chatModel ;
    MessageDetailsFragment chatFragment;
    private RecyclerView.Adapter myAdapter;
    ChatMessageDAO mDAO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        chatModel = new ViewModelProvider(this).get(ChatRoomModel.class);

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
         mDAO = db.cmDAO();

         chatModel.selectedMessage.observe(this, (newMessageValue) -> {

             chatFragment = new MessageDetailsFragment(newMessageValue);
             FragmentManager fMgr = getSupportFragmentManager();
             FragmentTransaction tx = fMgr.beginTransaction();
             tx.addToBackStack("");
             tx.add(R.id.fragmentLocation, chatFragment);
             tx.commit();

         });


        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());

        messages = chatModel.messages.getValue();
        if(messages == null)
            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());

        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() ->
                {
                    messages.addAll(mDAO.getAllMessages());

                    runOnUiThread(() -> binding.recycleView.setAdapter(myAdapter));
                });

        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.sendButton.setOnClickListener(clk -> {

            String editTextArray = binding.editTextArray.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(editTextArray, currentDateandTime, true);
            messages.add(chatMessage);


            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() ->
            {
               chatMessage.id=(int) mDAO.insertMessage(chatMessage);

            });


            myAdapter.notifyItemInserted(messages.size()-1);
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
                chatMessage.id=(int) mDAO.insertMessage(chatMessage);

            });

            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.editTextArray.setText("");
        });


        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (viewType == 0){
                SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                return new MyRowHolder(binding.getRoot());
            }

            else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.my_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        ChatMessage chatMessage = messages.get(position);
//        messages messageText = binding.getRoot();
//        int position = getAbsoluteAdapterPosition();

        switch(item.getItemId())
        {
            case R.id.item_1:

                ChatMessage removedMessage = chatModel.selectedMessage.getValue();
                int position = messages.indexOf(removedMessage);


                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                if (removedMessage != null && chatFragment != null) {
                    builder.setMessage("Do you want to delete this message: " + removedMessage.getMessage())
                            .setTitle("Question:")
                            .setNegativeButton("No", (dialog, cl) -> {
                            })
                            .setPositiveButton("Yes", (dialog, cl) -> {
                                messages.remove(position);
                                myAdapter.notifyItemRemoved(position);


                                Executor thread1 = Executors.newSingleThreadExecutor();
                                thread1.execute(() -> {
                                    getSupportFragmentManager().beginTransaction().remove(chatFragment).commit();
                                    mDAO.deleteMessage(removedMessage);
                                    chatFragment = null;

                                });


                                Snackbar.make(binding.getRoot(), "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                        .setAction("undo", clk2 -> {
                                            Executor thread2 = Executors.newSingleThreadExecutor();
                                            thread2.execute(() -> {
                                                long id = mDAO.insertMessage(removedMessage);
                                                removedMessage.id = id;
                                            });

                                            messages.add(position, removedMessage);
                                            myAdapter.notifyItemInserted(position);

                                        }).show();
                            }).create().show();
                }
                return true;

            case R.id.item_2:
                Toast.makeText(this, "Version 1.0, created by Christopher St.Aubin", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
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
                ChatMessage selected = messages.get(position);

                chatModel.selectedMessage.postValue(selected);
            });
        }
    }
}