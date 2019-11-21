package com.example.ruzbeh.chatapp.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ruzbeh.chatapp.Adapters.ChatListRVAdapter;
import com.example.ruzbeh.chatapp.Modules.Message;
import com.example.ruzbeh.chatapp.Modules.User;
import com.example.ruzbeh.chatapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int READ_CONTACTS_PERMISSION_REQUEST = 1;
    private static final String DEBUG = "iiifff";
    private static final int CONTACT_LOADER_ID = 2;
    private static final int LOOKUP_KEY_INDEX = 2;
    private RecyclerView recyclerView;
    private ChatListRVAdapter recyclerViewAdapter;
    private AlertDialog.Builder addContactDialogBuilder;
    private AlertDialog addContactDialog;

    Button bDialogAdd;
    View dialogView;
    private DocumentReference citiesRef;
    private FirebaseFirestore db;
    private ArrayList<User> chatListContacts;
    private String User_id;
    private String USER_ID = "user_id";
    private LayoutInflater inflater;
    private View viewMyLayout;
    private EditText contactEmail;
    private Query query;
    private String contact_id;
    private CollectionReference contactsRef;
    private ArrayList<String> arrayList;
    private String TAG = "IIIFFF";
    private CollectionReference usersRef;
    private DocumentReference contactsDocRef;
    private DocumentReference contactsListDocRef;
    private ArrayList<String> contactsList;
    private String contactUsername, contactPic;
    private Boolean contactOnline;
    private String contactUser_id;
    private String contactBio;
    private String contactEmaill;
    private CollectionReference userRef;
    private Query queryi;
    private ArrayList<User> contacts = new ArrayList<User>();
    private ProgressDialog dialog;
    private DocumentReference chatDocRef;
    private CollectionReference chatsRef;
    private ArrayList<Message> chats;
    private String chat_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactEmail = new EditText(this);
        contactEmail.setHint("...");

        addContactDialogBuilder = new AlertDialog.Builder(this);
        addContactDialogBuilder.setTitle("Enter User Email");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            addContactDialogBuilder.setView(contactEmail);
        }
        addContactDialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e("IIIFFF", "message : " + contactEmail.getText().toString());
                addContact(contactEmail.getText().toString());
            }
        });
        addContactDialog = addContactDialogBuilder.create();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                addContactDialog.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        db = FirebaseFirestore.getInstance();
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        User_id = users.getUid();

        chatListContacts = getContacts();

        recyclerView = (RecyclerView) findViewById(R.id.rv_chatList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new ChatListRVAdapter(this, chatListContacts);
        recyclerView.setAdapter(recyclerViewAdapter);

        //contacts list listener
        contactsListDocRef = db.collection("contacts").document(User_id);
        contactsListDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>()

        {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Log.e(TAG, "Current data: " + snapshot.getData());

                } else {
                    Log.e(TAG, "Current data: null");
                }
            }
        });
    }

    private void addContact(final String contactEmail) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("adding new contact ...");
        dialog.setIndeterminate(true);
        dialog.show();

        contactsDocRef = db.collection("contacts").document(User_id);
        usersRef = db.collection("users");
        query = (Query) usersRef.whereEqualTo("email", contactEmail.trim());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                try {
                    contact_id = documentSnapshots.getDocuments().get(0).getString("user_id");
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "user not exist", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "user not exist", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "user not exist", Toast.LENGTH_LONG).show();
                Log.e(TAG, "failure");
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "user not exist", Toast.LENGTH_LONG).show();
                return;
            }
        });

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(contactsDocRef);
                try {
                    ArrayList<String> contacts = (ArrayList<String>) snapshot.get("contacts_array");
                    if (!checkUserExistence(contact_id, contacts))
                        contacts.add(contact_id);
                    transaction.update(contactsDocRef, "contacts_array", contacts);


                } catch (Exception e) {
                    Log.e(TAG, "Transaction ffff!");
                    dialog.dismiss();
                    return null;
                }
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e(TAG, "Transaction success!");
                dialog.dismiss();
                chatListContacts = getContacts();
                recyclerViewAdapter.notifyDataSetChanged();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Transaction failure.", e);
                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "user not exist", Toast.LENGTH_LONG).show();

            }
        });

    }

    private boolean checkUserExistence(String contact_id, ArrayList<String> contacts) {
        for (int i = 0; i < contacts.size(); i++) {
            Log.e(TAG, contact_id + " -- " + contacts.get(i));
            if (contact_id.equals(contacts.get(i))) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<User> getContacts() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("loading contacts ...");
        dialog.setIndeterminate(true);
        dialog.show();

        if (chatListContacts != null) {
            chatListContacts.clear();
            recyclerViewAdapter.notifyDataSetChanged();
        }
        usersRef = db.collection("contacts");
        query = (Query) usersRef.whereEqualTo("user_id", User_id);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {

            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                contactsList = (ArrayList<String>) documentSnapshots.getDocuments().get(0).get("contacts_array");
                Log.e(TAG, "***" + contactsList);
                userRef = db.collection("users");
                for (int i = 0; i < contactsList.size(); i++) {
                    userRef = db.collection("users");
                    queryi = (Query) userRef.whereEqualTo("user_id", contactsList.get(i));
                    queryi.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {
                            contactUsername = documentSnapshots.getDocuments().get(0).getString("username");
                            contactOnline = documentSnapshots.getDocuments().get(0).getBoolean("online");
                            contactPic = documentSnapshots.getDocuments().get(0).getString("pic_url");
                            contactUser_id = documentSnapshots.getDocuments().get(0).getString("user_id");
                            contactBio = documentSnapshots.getDocuments().get(0).getString("bio");
                            contactEmaill = documentSnapshots.getDocuments().get(0).getString("email");
                            Log.e(TAG, contactUsername);
                            chatListContacts.add(new User(contactUsername, contactUser_id, null, contactEmaill, contactPic, contactBio, contactOnline));
                            recyclerViewAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "failed to load contacts", Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });
        return contacts;
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void openChat(String contactId) {
        dialog = new ProgressDialog(this);
        dialog.setMessage("loading messages ...");
        dialog.setIndeterminate(true);
        dialog.show();

        chat_id = "";
        for (int i = 0; i < contactId.length(); i++) {
            if (contactId.charAt(i) < User_id.charAt(i)) {
                chat_id = contactId + User_id;
                break;
            }
            if (contactId.charAt(i) > User_id.charAt(i)) {
                chat_id = User_id + contactId;
                break;
            }
        }

        chatsRef = db.collection("chats");
        query = (Query) usersRef.whereEqualTo("chat_id", chat_id.trim());
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot documentSnapshots) {
                Log.e(TAG, "******************* loading messages ***********");
                try {
                    chats = (ArrayList<Message>) documentSnapshots.getDocuments().get(0).get("messages");
                    Intent intent = new Intent(getApplication(), MessagingActivity.class);
                    intent.putExtra("messages", chats);
                    dialog.dismiss();
                    startActivity(intent);
                } catch (Exception e) {
                    Log.e(TAG, "******************* riiid messages ***********");
                    Map<String, Object> chat = new HashMap<>();
                    Log.e(TAG, "lets start chat !");
                    chat.put("chat_id", chat_id);
                    chat.put("messages", new ArrayList<Message>() {
                    });
                    db.collection("chats").document(chat_id).set(chat)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.e(TAG, "added  !");

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "not added !");
                                    Log.d("TAG", e.toString());
                                }
                            });
                }
                Log.e(TAG, chats.size() + "---**--");


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "******************* riiid messages ***********");
                Map<String, Object> chat = new HashMap<>();
                Log.e(TAG, "lets start chat !");
                chat.put("chat_id", chat_id);
                chat.put("messages", new ArrayList<Message>() {
                });

                db.collection("chats").document(chat_id).set(chat)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getBaseContext(), "added",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getBaseContext(), "ERROR" + e.toString(),
                                        Toast.LENGTH_LONG).show();
                                Log.d("TAG", e.toString());
                            }
                        });
                dialog.dismiss();
                return;
            }
        });
    }
}