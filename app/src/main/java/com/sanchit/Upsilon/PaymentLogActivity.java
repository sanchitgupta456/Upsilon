package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.sanchit.Upsilon.paymentLog.LogType;
import com.sanchit.Upsilon.paymentLog.PaymentLog;
import com.sanchit.Upsilon.paymentLog.PaymentLogsAdapter;

import org.bson.Document;

import java.util.ArrayList;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class PaymentLogActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String appID = "upsilon-ityvn";
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    MongoCollection<Document> mongoCollection;
    User user;
    ArrayList<PaymentLog> list = new ArrayList<>();
    PaymentLogsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_log);
        recyclerView = (RecyclerView) findViewById(R.id.container);
        app = new App(new AppConfiguration.Builder(appID)
                .build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        mongoCollection  = mongoDatabase.getCollection("Transactions");
        initRecycler();
    }

    private void initRecycler() {
        adapter = new PaymentLogsAdapter(list, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            getLogData();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getLogData() {
        //TODO: get log data;
        /* test: */
        Document queryFilter  = new Document("userid",user.getId());

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
        findTask.getAsync(task->{
            if(task.isSuccess())
            {
                MongoCursor<Document> results = task.get();
                while (results.hasNext())
                {
                    Document transaction = results.next();
                    if(transaction.get("type").equals("CREDITED"))
                    {
                        list.add(new PaymentLog(transaction.get("_id").toString(),"Bought the course - "+transaction.getString("courseName"),LogType.CREDITED, transaction.getLong("date"),transaction.getInteger("amount")));
                        adapter.notifyDataSetChanged();
                    }
                    else if(transaction.get("type").equals("WITHDRAW_PENDING"))
                    {
                        list.add(new PaymentLog(transaction.get("_id").toString(),"Withdrawl of Rs. " + String.valueOf(transaction.getInteger("amount")),LogType.WITHDRAW_PENDING, transaction.getLong("date"),transaction.getInteger("amount")));
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
//        list.add(new PaymentLog(LogType.DEBITED));
//        list.add(new PaymentLog(LogType.WITHDRAW_PENDING));
//        list.add(new PaymentLog(LogType.WITHDRAW_SUCCESS));
//        list.add(new PaymentLog(LogType.CREDITED));
        /* end test: */
    }
}