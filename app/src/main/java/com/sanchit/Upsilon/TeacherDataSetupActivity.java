package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.bson.Document;

import io.realm.mongodb.App;
import io.realm.mongodb.AppConfiguration;
import io.realm.mongodb.RealmResultTask;
import io.realm.mongodb.User;
import io.realm.mongodb.mongo.MongoClient;
import io.realm.mongodb.mongo.MongoCollection;
import io.realm.mongodb.mongo.MongoDatabase;
import io.realm.mongodb.mongo.iterable.MongoCursor;

public class TeacherDataSetupActivity extends AppCompatActivity {

    String appID = "upsilon-ityvn";
    private static final String TAG = "TeacherSetupActivity";
    HorizontalDotProgress progress;
    Button next;
    private QualificationsViewModel model0;
    private ExperienceViewModel model1;
    private PaymentDetailsViewModel model2;
    //TODO: Use these view models.

    private App app;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;
    private MongoCollection<Document> mongoCollection,mongoCollection1;
    private User user;
    private RelativeLayout relativeLayout;
    private String AccountNumber,IfscCode,MobileNumber,UpiId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_data_setup);
        app = new App(new AppConfiguration.Builder(appID).build());
        user = app.currentUser();
        mongoClient = user.getMongoClient("mongodb-atlas");
        mongoDatabase = mongoClient.getDatabase("Upsilon");
        mongoCollection  = mongoDatabase.getCollection("UserData");
        mongoCollection1  = mongoDatabase.getCollection("TeacherPaymentData");


        relativeLayout = (RelativeLayout) findViewById(R.id.teacher_data_setup_layout);
        progress = (HorizontalDotProgress) findViewById(R.id.dot_progress);
        next = (Button) findViewById(R.id.btnNext);
        model0 = new ViewModelProvider(this).get(QualificationsViewModel.class);
        model1 = new ViewModelProvider(this).get(ExperienceViewModel.class);
        model2 = new ViewModelProvider(this).get(PaymentDetailsViewModel.class);
        ViewPagerAdapter viewPagerAdapter =
                new ViewPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                progress.setDotPosition(position);
            }

            @Override
            public void onPageSelected(int position) {
                progress.setDotPosition(position);
                if(position == 2){
                    next.setText("Go");
                } else {
                    next.setText(R.string.next);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = viewPager.getCurrentItem();
                switch (pos){
                    case 0:
                        viewPager.setCurrentItem(1);
                        progress.setDotPosition(1);
                        break;
                    case 1:
                        viewPager.setCurrentItem(2);
                        progress.setDotPosition(2);
                        break;
                    case 2:
                        putData();
                        //TODO: Navigation to next activity from here.
                        //Intent intent = new Intent(getApplicationContext(), AddCourseActivity.class);
                        //startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public void putData(){
        Document queryFilter  = new Document("userid",user.getId());

        RealmResultTask<MongoCursor<Document>> findTask = mongoCollection.find(queryFilter).iterator();

        findTask.getAsync(task -> {
            if (task.isSuccess()) {
                MongoCursor<Document> results = task.get();
                if(!results.hasNext())
                {
                    Document document = new Document();
                    document = results.next();
                    document.append("teacherSetup",true);
                    document.append("userid",user.getId());
                    document.append("specialities",model0.getSpecialities().getValue());
                    document.append("qualifications",model1.getExperience().getValue());
                    document.append("experience",model1.getExperience().getValue());
                    mongoCollection.insertOne(document)
                            .getAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                            + result.get().getInsertedId());
                                    goToSetupActivity();
                                } else {
                                    Snackbar.make(relativeLayout,"An error occured . Please signIn again",Snackbar.LENGTH_LONG).show();
                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                }
                            });
                }
                else
                {
                    Document document = new Document();
                    document = results.next();
                    document.append("teacherSetup",true);
                    document.append("specialities",model0.getSpecialities().getValue());
                    document.append("qualifications",model1.getExperience().getValue());
                    document.append("experience",model1.getExperience().getValue());
                    mongoCollection.updateOne(new Document("userid",user.getId()),
                            document)
                            .getAsync(result -> {
                                if (result.isSuccess()) {
                                    Log.v("EXAMPLE", "Inserted custom user data document. _id of inserted document: "
                                            + result.get().getModifiedCount());
                                    startActivity(new Intent(TeacherDataSetupActivity.this, MainActivity.class));
                                } else {
                                    Snackbar.make(relativeLayout,"An error occured . Please signIn again",Snackbar.LENGTH_LONG).show();
                                    Log.e("EXAMPLE", "Unable to insert custom user data. Error: " + result.getError());
                                }
                            });
                }

            } else {
                Snackbar.make(relativeLayout,"An error occured . Please signIn again",Snackbar.LENGTH_LONG).show();
                Log.v("User","Failed to complete search");
            }
        });

        AccountNumber = model2.getAccount_number().getValue();
        IfscCode = model2.getIfsc_code().getValue();
        MobileNumber = model2.getMobile_number().getValue();
        UpiId = model2.getUpi_id().getValue();
        RealmResultTask<MongoCursor<Document>> findTask1 = mongoCollection1.find(queryFilter).iterator();

        if(UpiId==null || UpiId.isEmpty())
        {
            if(AccountNumber==null || AccountNumber.isEmpty())
            {
//                Animation shake = AnimationUtils.loadAnimation(AddCoursePayment.this, R.anim.shake);
//                accountNumber.startAnimation(shake);
//                accountNumber.setError("Please Enter either bank details or UPI Id");
//                accountNumber.requestFocus();
//                upiId.startAnimation(shake);
//                upiId.setError("Please Enter either bank details or UPI Id");
//                upiId.requestFocus();
//                Snackbar.make(relativeLayout,"Please enter either bank details or UPI Id",Snackbar.LENGTH_LONG).show();
            }
            else
            {
                findTask1.getAsync(result -> {
                    if(result.isSuccess())
                    {
                        MongoCursor<Document> results = result.get();
                        if(results.hasNext())
                        {
                            Document payment = results.next();
//                            accountNumber.setText(payment.getString("accountNumber"));
//                            ifscCode.setText(payment.getString("ifscCode"));
//                            mobileNumber.setText(payment.getString("mobileNumber"));
//                            upiId.setText(payment.getString("UpiId"));
                            payment.append("accountNumber",AccountNumber);
                            payment.append("ifscCode",IfscCode);
                            payment.append("mobileNumber",MobileNumber);
                            payment.append("UpiId",UpiId);

                            mongoCollection1.updateOne(new Document("userid",user.getId()),payment).getAsync(result1 -> {
                                if(result1.isSuccess())
                                {
                                    startActivity(new Intent(TeacherDataSetupActivity.this, MainActivity.class));
                                }
                                else
                                {
                                    Snackbar.make(relativeLayout,"Please try again later",Snackbar.LENGTH_LONG).show();
                                }
                            });
                        }
                        else
                        {
                            mongoCollection1.insertOne(new Document("userid", user.getId()).append("accountNumber", AccountNumber).append("ifscCode", IfscCode).append("mobileNumber", MobileNumber).append("UpiId", UpiId).append("WalletAmountToBePaid",0)).getAsync(result1 -> {
                                if (result1.isSuccess()) {
                                    startActivity(new Intent(TeacherDataSetupActivity.this, MainActivity.class));
                                } else {
                                    Snackbar.make(relativeLayout,"Please try again later",Snackbar.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    else
                    {

                    }
                });

            }
        }
        else {
            findTask1.getAsync(result -> {
                if(result.isSuccess())
                {
                    MongoCursor<Document> results = result.get();
                    if(results.hasNext())
                    {
                        Document payment = results.next();
//                        accountNumber.setText(payment.getString("accountNumber"));
//                        ifscCode.setText(payment.getString("ifscCode"));
//                        mobileNumber.setText(payment.getString("mobileNumber"));
//                        upiId.setText(payment.getString("UpiId"));
                        payment.append("accountNumber",AccountNumber);
                        payment.append("ifscCode",IfscCode);
                        payment.append("mobileNumber",MobileNumber);
                        payment.append("UpiId",UpiId);

                        mongoCollection1.updateOne(new Document("userid",user.getId()),payment).getAsync(result1 -> {
                            if(result1.isSuccess())
                            {
                                startActivity(new Intent(TeacherDataSetupActivity.this, MainActivity.class));

                            }
                            else
                            {
                                Snackbar.make(relativeLayout,"Please try again later",Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {
                        mongoCollection1.insertOne(new Document("userid", user.getId()).append("accountNumber", AccountNumber).append("ifscCode", IfscCode).append("mobileNumber", MobileNumber).append("UpiId", UpiId).append("WalletAmountToBePaid",0)).getAsync(result1 -> {
                            if (result1.isSuccess()) {
                                startActivity(new Intent(TeacherDataSetupActivity.this, MainActivity.class));
                            } else {
                                Snackbar.make(relativeLayout,"Please try again later",Snackbar.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                else
                {
                    Snackbar.make(relativeLayout,"Please try again later",Snackbar.LENGTH_LONG).show();
                }
            });
        }
        //get data from model0, model1 and model2 using getter and then getValue
        //eg. model0.getSpecialities().getValue()
    }

    private void goToSetupActivity() {
        Intent intent = new Intent(TeacherDataSetupActivity.this,UserDataSetupActivity.class);
        startActivity(intent);
    }

    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final Context mContext;
        public ViewPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            if (position == 0) {
                return QualificationsFragment.newInstance();
            } else if (position == 1){
                return ExperienceFragment.newInstance();
            } else {
                return PaymentDetailsFragment.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}