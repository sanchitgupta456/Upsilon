package com.sanchit.Upsilon.testData;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
        if (questions.get(position).getMode()==Mode.REVIEW)
            holder.receivedMarksTvShow.setText(String.valueOf(questions.get(position).getMarks_received()));
        else
            holder.receivedMarksTvShow.setVisibility(View.GONE);
        holder.questionTextTvShow.setText(questions.get(position).getQuestionText());
        holder.mcqGroupShow.setVisibility(View.GONE);
        holder.answerLayoutTilShow.setVisibility(View.GONE);
        holder.rvFiles.setVisibility(View.GONE);
        holder.btnUpload.setVisibility(View.GONE);
        holder.reviewAnswerTextTvShow.setVisibility(View.GONE);
        if(questions.get(position).isMCQSingle()) {
            holder.mcqGroupShow.setVisibility(View.VISIBLE);
            holder.mcqGroupShow.setSingleSelection(true);
            holder.mcqGroupShow.removeAllViews();
            if (questions.get(position).getMode()==Mode.REVIEW) {
                for (int i = 0; i < questions.get(position).getNumMCQOptions(); i++) {
                    Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option,holder.mcqGroupShow, false);
                    chip.setText(questions.get(position).getMcqOptionsList().get(i));
                    chip.setClickable(false);
                    chip.setChecked(false);
                    holder.mcqGroupShow.addView(chip);
                }
                ArrayList<Integer> arr = questions.get(position).getMarkedMCQOptions();
                for (int i = 0; i < arr.size(); i++) {
                    Chip chip = (Chip) holder.mcqGroupShow.getChildAt(arr.get(i));
                    chip.setChecked(true);
                }
            }
            if (questions.get(position).getMode()==Mode.ANSWERING) {
                for (int i = 0; i < questions.get(position).getNumMCQOptions(); i++) {
                    Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option,holder.mcqGroupShow, false);
                    chip.setText(questions.get(position).getMcqOptionsList().get(i));
                    chip.setClickable(true);
                    chip.setChecked(false);
//                    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            buttonView.setChecked(isChecked);
//                        }
//                    });
                    holder.mcqGroupShow.addView(chip);
                }
                ArrayList<Integer> arr = questions.get(position).getMarkedMCQOptions();
                for (int i = 0; i < arr.size(); i++) {
                    Chip chip = (Chip) holder.mcqGroupShow.getChildAt(arr.get(i));
                    chip.setChecked(true);
                }
                holder.mcqGroupShow.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ChipGroup group, int checkedId) {
                        ArrayList<Integer> arr1 = new ArrayList<>();
                        int test = 0;
                        for (int i = 0; i < group.getChildCount(); i++) {
                            Chip chip1 = (Chip) holder.mcqGroupShow.getChildAt(i);
                            if (chip1.isChecked()) {
                                arr1.add(i);
                                test = i + 1;
                            }
                        }
                        questions.get(position).setMarkedMCQOptions(arr1);
                        Toast.makeText(context, "Option "+ test, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (questions.get(position).getMode()==Mode.CREATING) {
                holder.mcqGroupShow.setVisibility(View.GONE);
                //TODO: Construct options adding mechanism
//                for (int i = 0; i < questions.get(position).getNumMCQOptions(); i++) {
//                    Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option,holder.mcqGroupShow, false);
//                    chip.setText(questions.get(position).getMcqOptionsList().get(i));
//                    chip.setClickable(false);
//                    chip.setChecked(false);
//                    holder.mcqGroupShow.addView(chip);
//                }
            }
        }
        if(questions.get(position).isMCQMultiple()) {
            holder.mcqGroupShow.setVisibility(View.VISIBLE);
            holder.mcqGroupShow.setSingleSelection(false);
            holder.mcqGroupShow.removeAllViews();
            if (questions.get(position).getMode()==Mode.REVIEW) {
                for (int i = 0; i < questions.get(position).getNumMCQOptions(); i++) {
                    Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option,holder.mcqGroupShow, false);
                    chip.setText(questions.get(position).getMcqOptionsList().get(i));
                    chip.setClickable(false);
                    chip.setChecked(false);
                    holder.mcqGroupShow.addView(chip);
                }
                ArrayList<Integer> arr = questions.get(position).getMarkedMCQOptions();
                for (int i = 0; i < arr.size(); i++) {
                    Chip chip = (Chip) holder.mcqGroupShow.getChildAt(arr.get(i));
                    chip.setChecked(true);
                }
            }
            if (questions.get(position).getMode()==Mode.ANSWERING) {
                for (int i = 0; i < questions.get(position).getNumMCQOptions(); i++) {
                    Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option,holder.mcqGroupShow, false);
                    chip.setText(questions.get(position).getMcqOptionsList().get(i));
                    chip.setClickable(true);
                    chip.setChecked(false);
//                    chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            buttonView.setChecked(isChecked);
//                        }
//                    });
                    holder.mcqGroupShow.addView(chip);
                }
                ArrayList<Integer> arr = questions.get(position).getMarkedMCQOptions();
                for (int i = 0; i < arr.size(); i++) {
                    Chip chip = (Chip) holder.mcqGroupShow.getChildAt(arr.get(i));
                    chip.setChecked(true);
                }
                holder.mcqGroupShow.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(ChipGroup group, int checkedId) {
                        ArrayList<Integer> arr1 = new ArrayList<>();
                        for (int i = 0; i < group.getChildCount(); i++) {
                            Chip chip1 = (Chip) holder.mcqGroupShow.getChildAt(i);
                            if (chip1.isChecked()) arr1.add(i);
                        }
                        questions.get(position).setMarkedMCQOptions(arr1);
                    }
                });
            }
            if (questions.get(position).getMode()==Mode.CREATING) {
                holder.mcqGroupShow.setVisibility(View.GONE);
                //TODO: Construct options adding mechanism
//                for (int i = 0; i < questions.get(position).getNumMCQOptions(); i++) {
//                    Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option,holder.mcqGroupShow, false);
//                    chip.setText(questions.get(position).getMcqOptionsList().get(i));
//                    chip.setClickable(false);
//                    chip.setChecked(false);
//                    holder.mcqGroupShow.addView(chip);
//                }
            }
        }
        if(questions.get(position).isTextEnabled()) {
            if(questions.get(position).getMode()==Mode.ANSWERING) {
                holder.answerLayoutTilShow.setVisibility(View.VISIBLE);
                holder.answerTextTiEtShow.setText(questions.get(position).getAnswerText());
                holder.textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        questions.get(position).setAnswerText(s.toString());
//                        Toast.makeText(context, "Written "+questions.get(position).getAnswerText(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                };
                holder.answerTextTiEtShow.addTextChangedListener(holder.textWatcher);
                holder.reviewAnswerTextTvShow.setVisibility(View.GONE);
            }
            if(questions.get(position).getMode()==Mode.REVIEW) {
                holder.answerLayoutTilShow.setVisibility(View.GONE);
                holder.reviewAnswerTextTvShow.setVisibility(View.VISIBLE);
                holder.reviewAnswerTextTvShow.setText(questions.get(position).getAnswerText());
            }
            if(questions.get(position).getMode()==Mode.CREATING) {
                holder.answerLayoutTilShow.setVisibility(View.GONE);
                holder.reviewAnswerTextTvShow.setVisibility(View.GONE);
            }
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
        TextView reviewAnswerTextTvShow;
        TextWatcher textWatcher;

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
            reviewAnswerTextTvShow = (TextView) itemView.findViewById(R.id.review_answer_text);
        }

    }
}
