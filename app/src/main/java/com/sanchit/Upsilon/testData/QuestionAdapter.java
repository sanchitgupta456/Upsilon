package com.sanchit.Upsilon.testData;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.sanchit.Upsilon.R;
import com.sanchit.Upsilon.courseData.CoursesAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private static final String TAG = "QuestionAdapter";

    private ArrayList<Question> questions;
    private Context context;

    public QuestionAdapter(ArrayList<Question> questions, Context context) {
        this.questions = questions;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_question,parent,false);
        QuestionAdapter.ViewHolder viewHolder = new QuestionAdapter.ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.questionNumberTvShow.setText(String.valueOf(questions.get(position).getNumber()));
        Log.d(TAG, "onBindViewHolder: number is :" + questions.get(position).getNumber());
        holder.fullMarksTvShow.setText(String.valueOf(questions.get(position).getFull_marks()));
        holder.receivedMarksTvShow.setText(String.valueOf(questions.get(position).getMarks_received()));
        holder.questionTextTvShow.setText(questions.get(position).getQuestionText());
        holder.mcqGroupShow.setVisibility(View.GONE);
        holder.answerLayoutTilShow.setVisibility(View.GONE);
        holder.rvFiles.setVisibility(View.GONE);
        holder.btnUpload.setVisibility(View.GONE);
        if(questions.get(position).isMCQSingle()) {
            holder.mcqGroupShow.setVisibility(View.VISIBLE);
            holder.mcqGroupShow.setSingleSelection(true);
            holder.mcqGroupShow.removeAllViews();
            for (int i = 0; i < questions.get(position).getNumMCQOptions(); i++) {
                Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option,holder.mcqGroupShow, false);
                chip.setText(questions.get(position).getMcqOptionsList().get(i));
                //TODO: do something on chip click
                holder.mcqGroupShow.addView(chip);
            }
        }
        if(questions.get(position).isMCQMultiple()) {
            holder.mcqGroupShow.setVisibility(View.VISIBLE);
            holder.mcqGroupShow.setSingleSelection(false);
            holder.mcqGroupShow.removeAllViews();
            for (int i = 0; i < questions.get(position).getNumMCQOptions(); i++) {
                Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option,holder.mcqGroupShow, false);
                chip.setText(questions.get(position).getMcqOptionsList().get(i));
                //TODO: do something on chip click
                holder.mcqGroupShow.addView(chip);
            }
        }
        if(questions.get(position).isTextEnabled()) {
            holder.answerLayoutTilShow.setVisibility(View.VISIBLE);
            holder.answerTextTiEtShow.setText(questions.get(position).getAnswerText());
            //TODO: Handle/update changes to the question
        }
        if (questions.get(position).isFileUploadEnabled()) {
            holder.rvFiles.setVisibility(View.VISIBLE);
            holder.btnUpload.setVisibility(View.VISIBLE);

            //TODO: Handle file upload
        }
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView questionNumberTvShow;
        TextView receivedMarksTvShow;
        TextView fullMarksTvShow;
        TextView questionTextTvShow;
        TextInputEditText answerTextTiEtShow;
        TextInputLayout answerLayoutTilShow;
        ChipGroup mcqGroupShow;
        MaterialCardView cv;
        RecyclerView rvFiles;
        //File List Adapter
        Button btnUpload;

        public ViewHolder(View itemView)
        {
            super(itemView);
            questionNumberTvShow = (TextView)itemView.findViewById(R.id.question_number);
            receivedMarksTvShow = (TextView)itemView.findViewById(R.id.marks_received);
            fullMarksTvShow = (TextView) itemView.findViewById(R.id.full_marks);
            questionTextTvShow = (TextView) itemView.findViewById(R.id.question_text);
            answerTextTiEtShow = (TextInputEditText) itemView.findViewById(R.id.answer_text);
            answerLayoutTilShow = (TextInputLayout) itemView.findViewById(R.id.answer_layout);
            mcqGroupShow = (ChipGroup) itemView.findViewById(R.id.mcq_options);
            cv = (MaterialCardView) itemView.findViewById(R.id.cv);
            rvFiles = (RecyclerView)itemView.findViewById(R.id.attachments);
            btnUpload = (Button) itemView.findViewById(R.id.upload);

        }

    }
}
