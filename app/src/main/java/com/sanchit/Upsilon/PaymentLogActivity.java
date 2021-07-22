package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.sanchit.Upsilon.paymentLog.LogType;
import com.sanchit.Upsilon.paymentLog.PaymentLog;
import com.sanchit.Upsilon.paymentLog.PaymentLogsAdapter;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private RequestQueue queue;
    private String API ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_log);
        recyclerView = (RecyclerView) findViewById(R.id.container);
        queue = Volley.newRequestQueue(getApplicationContext());
        API = ((Upsilon)this.getApplication()).getAPI();
//        app = new App(new AppConfiguration.Builder(appID)
//                .build());
//        user = app.currentUser();
//        mongoClient = user.getMongoClient("mongodb-atlas");
//        mongoDatabase = mongoClient.getDatabase("Upsilon");
//        mongoCollection  = mongoDatabase.getCollection("Transactions");
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
//        Document queryFilter  = new Document("userid",user.getId());

//        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();
//        findTask.getAsync(task->{
//            if(task.isSuccess())
//            {
//                MongoCursor<Document> results = task.get();
//                while (results.hasNext())
//                {
//                    Document transaction = results.next();
//                    if(transaction.get("type").equals("CREDITED"))
//                    {
//                        list.add(new PaymentLog(transaction.get("_id").toString(),"Bought the course - "+transaction.getString("courseName"),LogType.CREDITED, transaction.getLong("date"),transaction.getInteger("amount")));
//                        adapter.notifyDataSetChanged();
//                    }
//                    else if(transaction.get("type").equals("WITHDRAW_PENDING"))
//                    {
//                        list.add(new PaymentLog(transaction.get("_id").toString(),"Withdrawl of Rs. " + String.valueOf(transaction.getInteger("amount")),LogType.WITHDRAW_PENDING, transaction.getLong("date"),transaction.getInteger("amount")));
//                        adapter.notifyDataSetChanged();
//                    }
//                }
//            }
//        });
            JSONObject jsonObject = new JSONObject();
            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, API+"/transactions",jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("FetchTutorNameLoc", response.toString());
                            try {
                                JSONArray jsonArray = (JSONArray) response.get("details");
                                for(int i=0;i<jsonArray.length();i++)
                                {
                                    JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                                    if(jsonObject1.getString("type").equals("DEBITED"))
                                    {
                                        list.add(new PaymentLog(jsonObject1.getString("transactionId"),"Bought course " + jsonObject1.get("courseId"),LogType.DEBITED,jsonObject1.getInt("amount"),jsonObject1.getLong("date")));
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @SuppressLint("LongLogTag")
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("ErrorFetchingTutorNameLoc", error.toString());
                        }
                    }
            ){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", ((Upsilon)getApplication()).getToken());
                    return params;
                }
            };
            queue.add(jsonRequest);

//        list.add(new PaymentLog(LogType.DEBITED));
//        list.add(new PaymentLog(LogType.WITHDRAW_PENDING));
//        list.add(new PaymentLog(LogType.WITHDRAW_SUCCESS));
//        list.add(new PaymentLog(LogType.CREDITED));
        /* end test: */
    }
}