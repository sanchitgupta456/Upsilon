package com.sanchit.Upsilon.paymentLog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sanchit.Upsilon.R;

import java.util.ArrayList;

public class PaymentLogsAdapter extends RecyclerView.Adapter<PaymentLogsAdapter.ViewHolder>{
    private ArrayList<PaymentLog> logs;
    private Context context;

    public PaymentLogsAdapter(ArrayList<PaymentLog> logs, Context context) {
        this.logs = logs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_log_recycler_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PaymentLog log = logs.get(position);
        //holder.tvId.setText(log.getTransactionId());
        switch (log.getType()) {
            case CREDITED:
                //holder.tvMessageCredited.setText(log.getLogMessage());
                holder.tvMessageCredited.setVisibility(View.VISIBLE);
                break;
            case DEBITED:
                //holder.tvMessageDebited.setText(log.getLogMessage());
                holder.tvMessageDebited.setVisibility(View.VISIBLE);
                break;
            case WITHDRAW_PENDING:
                //holder.tvMessagePending.setText(log.getLogMessage());
                holder.tvMessagePending.setVisibility(View.VISIBLE);
                break;
            case WITHDRAW_SUCCESS:
                //holder.tvMessageSuccess.setText(log.getLogMessage());
                holder.tvMessageSuccess.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return logs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvMessageCredited, tvMessageDebited, tvMessageSuccess, tvMessagePending, tvDateTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.transaction_id);
            tvMessageCredited = itemView.findViewById(R.id.messageCredited);
            tvMessageDebited = itemView.findViewById(R.id.messageDebited);
            tvMessageSuccess = itemView.findViewById(R.id.messageWithdrawalSuccessful);
            tvMessagePending = itemView.findViewById(R.id.messageWithdrawalPending);
            tvDateTime = itemView.findViewById(R.id.date_time);
        }
    }
}
