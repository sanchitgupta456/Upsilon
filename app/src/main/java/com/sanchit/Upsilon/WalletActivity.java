package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.bson.Document;
import org.w3c.dom.Text;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;


public class WalletActivity extends AppCompatActivity {

    private TextView accountnumber,ifsc,mobile,upi,amountdue;
    private String Accountnumber;
    private String Ifsc;
    private String Mobile;
    private String Upi;
    private Integer AmountDue;
    String appID = "upsilon-ityvn";
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    Button EditPaymentDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upsilon Wallet");
        getSupportActionBar().setElevation(10);

        accountnumber = (TextView) findViewById(R.id.textAccountNumber);
        ifsc = (TextView) findViewById(R.id.textIFSCCode);
        mobile = (TextView) findViewById(R.id.textMobileNumber);
        upi = (TextView) findViewById(R.id.textUPIId);
        amountdue = (TextView) findViewById(R.id.amount_due);
        EditPaymentDetails = (Button) findViewById(R.id.btn_edit_payment_details);

//        accountnumber.setText(Accountnumber);
//        ifsc.setText(Ifsc);
//        mobile.setText(Mobile);
//        upi.setText(Upi);
//        amountdue.setText(AmountDue);

        EditPaymentDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WalletActivity.this,AddCoursePayment.class));
            }
        });

        app = new App(new AppConfiguration.Builder(appID)
                .build());
        User user = app.currentUser();

        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        MongoCollection<Document> mongoCollection  = mongoDatabase.getCollection("TeacherPaymentData");

        //Blank query to find every single course in db
        //TODO: Modify query to look for user preferred course IDs
        Document queryFilter  = new Document("userid",user.getId());

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task->{
            if(task.isSuccess())
            {
                MongoCursor<Document> results = task.get();
                if(results.hasNext()) {
                    Document paymentDetails = results.next();
                    Accountnumber = paymentDetails.getString("accountNumber");
                    Ifsc = paymentDetails.getString("ifscCode");
                    Mobile = paymentDetails.getString("mobileNumber");
                    Upi = paymentDetails.getString("UpiId");
                    AmountDue = paymentDetails.getInteger("WalletAmountToBePaid");
                    accountnumber.setText(Accountnumber);
                    ifsc.setText(Ifsc);
                    mobile.setText(Mobile);
                    upi.setText(Upi);
                    amountdue.setText(String.valueOf(AmountDue));
                    Log.v("WalletDetails",Accountnumber+Ifsc+Mobile+Upi);
                }
                else
                {
                    Log.v("Wallet Activity","Error");
                }
            }
            else
            {
                Log.v("Wallet Error",task.getError().toString());
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Options");
        return true;
    }*/
}