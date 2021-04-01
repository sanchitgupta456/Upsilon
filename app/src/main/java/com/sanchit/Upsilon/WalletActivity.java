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
import android.widget.Toast;

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

    private TextView accountnumber,ifsc,mobile,upi,amountdue,Amount;
    private String Accountnumber;
    private String Ifsc;
    private String Mobile;
    private String Upi;
    private Integer AmountDue;
    private Integer amount;
    String appID = "upsilon-ityvn";
    App app;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;
    Button EditPaymentDetails;
    Button Withdraw;
    Document paymentDetails;

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
        Withdraw = (Button) findViewById(R.id.btn_withdraw_from_wallet);
        Amount = (TextView) findViewById(R.id.current_balance);
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
                    paymentDetails = results.next();
                    Accountnumber = paymentDetails.getString("accountNumber");
                    Ifsc = paymentDetails.getString("ifscCode");
                    Mobile = paymentDetails.getString("mobileNumber");
                    Upi = paymentDetails.getString("UpiId");
                    AmountDue = paymentDetails.getInteger("WalletAmountToBePaid");
                    try {
                        amount = paymentDetails.getInteger("walletAmount");
                        Log.v("amount",amount.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        amount=0;
                    }
                    accountnumber.setText(Accountnumber);
                    try {
                        Amount.setText("Rs. " +amount.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ifsc.setText(Ifsc);
                    mobile.setText(Mobile);
                    upi.setText(Upi);
                    if(AmountDue==null)
                    {
                        AmountDue=0;
                    }
                    amountdue.setText("Rs. "+String.valueOf(AmountDue));
                    Log.v("WalletDetails",Accountnumber+Ifsc+Mobile+Upi);
                }
                else
                {
                    Log.v("Wallet Activity","Error No Results For Payment Details");
                }
            }
            else
            {
                Log.v("Wallet Error",task.getError().toString());
            }
        });

        Withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MongoCollection<Document> mongoCollection3  = mongoDatabase.getCollection("Transactions");
                MongoCollection<Document> mongoCollection4  = mongoDatabase.getCollection("WalletAmount");
                Document queryFilter1  = new Document("userid",user.getId());
                RealmResultTask<MongoCursor<Document>> findTask2 = mongoCollection3.find(queryFilter1).iterator();
                RealmResultTask<MongoCursor<Document>> findTask3 = mongoCollection4.find(queryFilter1).iterator();

                Document transaction = new Document();
                transaction.append("date",System.currentTimeMillis());
                transaction.append("userid",user.getId());
                transaction.append("type", "WITHDRAW_PENDING");
//                transaction.append("tutorId",course.getTutorId());
//                transaction.append("courseId",course.getCourseId());
//                transaction.append("courseName",course.getCourseName());
                transaction.append("amount",amount);

                paymentDetails.append("walletAmount",0);
                mongoCollection.updateOne(queryFilter,paymentDetails).getAsync(result -> {
                    if(result.isSuccess())
                    {
                        Log.v("Amount Deduction","Success");
                    }
                    else
                    {
                        Log.v("Amount Deduction","Error");
                    }
                });

                mongoCollection3.insertOne(transaction).getAsync(result -> {
                    if(result.isSuccess())
                    {
                        Toast.makeText(getApplicationContext(),"Withdrawl Request Submitted Successfully",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Withdrawl Request Error",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_teacher_info, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        if(item.getItemId() == R.id.notification) {
            startActivity(new Intent(getApplicationContext(), PaymentLogActivity.class));
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