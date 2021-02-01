package com.sanchit.Upsilon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;

import com.sanchit.Upsilon.paymentLog.LogType;
import com.sanchit.Upsilon.paymentLog.PaymentLog;
import com.sanchit.Upsilon.paymentLog.PaymentLogsAdapter;

import java.util.ArrayList;

public class PaymentLogActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<PaymentLog> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_log);
        recyclerView = (RecyclerView) findViewById(R.id.container);
        initRecycler();
    }

    private void initRecycler() {
        getLogData();
        PaymentLogsAdapter adapter = new PaymentLogsAdapter(list, getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(recyclerView.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void getLogData() {
        //TODO: get log data;
        /* test: */
        list.add(new PaymentLog(LogType.CREDITED));
        list.add(new PaymentLog(LogType.DEBITED));
        list.add(new PaymentLog(LogType.WITHDRAW_PENDING));
        list.add(new PaymentLog(LogType.WITHDRAW_SUCCESS));
        list.add(new PaymentLog(LogType.CREDITED));
        /* end test: */
    }
}