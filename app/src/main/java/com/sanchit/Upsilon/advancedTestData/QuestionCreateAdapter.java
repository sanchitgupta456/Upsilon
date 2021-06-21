package com.sanchit.Upsilon.advancedTestData;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.sanchit.Upsilon.R;

import java.util.ArrayList;

public class QuestionCreateAdapter extends RecyclerView.Adapter<QuestionCreateAdapter.ViewHolder>{

    private static final String TAG = "QuestionCreateAdapter";

    private ArrayList<Question> questions;
    private Context context;

    public QuestionCreateAdapter(ArrayList<Question> questions, Context context) {
        this.questions = questions;
        if(this.questions == null) this.questions = new ArrayList<Question>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_question_create,parent,false);
        QuestionCreateAdapter.ViewHolder viewHolder = new QuestionCreateAdapter.ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question q = questions.get(position);

        holder.llEditFrame.setVisibility(View.GONE);
        holder.llMCQFrame.setVisibility(View.GONE);
        holder.llDirectAnswerFrame.setVisibility(View.GONE);
        holder.llFileUpload.setVisibility(View.GONE);

        int defaultQuestionNumber = position + 1;
        holder.questionNumberTvShow.setText(""+defaultQuestionNumber);
        holder.questionNumberTvShow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null || s.toString().equals(""))
                    q.setFull_marks(0);
                else
                    q.setFull_marks(Integer.parseInt(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //optional title not used

        holder.fullMarksTvShow.setText(""+q.getFull_marks());
        holder.fullMarksTvShow.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null || s.toString().equals(""))
                    q.setFull_marks(0);
                else
                    q.setFull_marks(Integer.parseInt(s.toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.btnDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.llEditFrame.getVisibility()==View.VISIBLE) {
                    holder.llEditFrame.setVisibility(View.GONE);
                } else {
                    holder.llEditFrame.setVisibility(View.VISIBLE);
                }
                v.animate().rotationBy(180).start();
            }
        });

        String[] types = {"Single Alternative MCQ", "Multiple Correct MCQ", "Direct Text Answers"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.simple_expandable_list_item_custom, types);
        holder.typeSpinner.setAdapter(adapter);
        holder.typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = parent.getItemAtPosition(position).toString();
                if(s.equals("Single Alternative MCQ")) {
                    q.setMCQSingle(true);
                    q.setMCQMultiple(false);
                    q.setTextEnabled(false);
                    holder.llDirectAnswerFrame.setVisibility(View.GONE);
                    holder.llMCQFrame.setVisibility(View.VISIBLE);
                    holder.mcqGroupShow.setSingleSelection(true);
                    if(q.getMcqOptionsList()==null) {
                        q.setNumMCQOptions(0);
                        q.setMcqOptionsList(new ArrayList<>());
                    }
                    if (q.getMcqOptionsList().size()==0) {
                        q.setNumMCQOptions(0);
                    }
                    holder.mcqGroupShow.removeAllViews();
                    for (int i = 0; i < questions.get(position).getNumMCQOptions(); i++) {
                        Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option_create,holder.mcqGroupShow, false);
                        chip.setText(questions.get(position).getMcqOptionsList().get(i));
                        chip.setChecked(false);
                        holder.mcqGroupShow.addView(chip);
                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.mcqGroupShow.removeView(v);
                                q.setNumMCQOptions(q.getNumMCQOptions()-1);
                                Chip chip1 = (Chip) v;
                                q.getMcqOptionsList().remove(chip1.getText().toString());
                            }
                        });
                    }
                    holder.btnAddOption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String s = holder.enterOption.getText().toString().trim();
                            Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option_create,holder.mcqGroupShow, false);
                            chip.setText(s);
                            chip.setChecked(false);
                            holder.mcqGroupShow.addView(chip);
                            q.getMcqOptionsList().add(s);
                            q.setNumMCQOptions(q.getNumMCQOptions()+1);
                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    holder.mcqGroupShow.removeView(v);
                                    q.setNumMCQOptions(q.getNumMCQOptions()-1);
                                    Chip chip1 = (Chip) v;
                                    q.getMcqOptionsList().remove(chip1.getText().toString());
                                }
                            });
                            holder.enterOption.setText("");
                        }
                    });
                } else if(s.equals("Multiple Correct MCQ")) {
                    q.setMCQSingle(false);
                    q.setMCQMultiple(true);
                    q.setTextEnabled(false);
                    holder.llDirectAnswerFrame.setVisibility(View.GONE);
                    holder.llMCQFrame.setVisibility(View.VISIBLE);
                    holder.mcqGroupShow.setSingleSelection(false);
                    if(q.getMcqOptionsList()==null) {
                        q.setNumMCQOptions(0);
                        q.setMcqOptionsList(new ArrayList<>());
                    }
                    if (q.getMcqOptionsList().size()==0) {
                        q.setNumMCQOptions(0);
                    }
                    holder.mcqGroupShow.removeAllViews();
                    for (int i = 0; i < questions.get(position).getNumMCQOptions(); i++) {
                        Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option_create,holder.mcqGroupShow, false);
                        chip.setText(questions.get(position).getMcqOptionsList().get(i));
                        chip.setChecked(false);
                        holder.mcqGroupShow.addView(chip);
                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                holder.mcqGroupShow.removeView(v);
                                q.setNumMCQOptions(q.getNumMCQOptions()-1);
                                Chip chip1 = (Chip) v;
                                q.getMcqOptionsList().remove(chip1.getText().toString());
                            }
                        });
                    }
                    holder.btnAddOption.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String s = holder.enterOption.getText().toString().trim();
                            Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option_create,holder.mcqGroupShow, false);
                            chip.setText(s);
                            chip.setChecked(false);
                            holder.mcqGroupShow.addView(chip);
                            q.getMcqOptionsList().add(s);
                            q.setNumMCQOptions(q.getNumMCQOptions()+1);
                            chip.setOnCloseIconClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    holder.mcqGroupShow.removeView(v);
                                    q.setNumMCQOptions(q.getNumMCQOptions()-1);
                                    Chip chip1 = (Chip) v;
                                    q.getMcqOptionsList().remove(chip1.getText().toString());
                                }
                            });
                            holder.enterOption.setText("");
                        }
                    });
                } else if(s.equals("Direct Text Answers")) {
                    q.setMCQSingle(false);
                    q.setMCQMultiple(false);
                    q.setTextEnabled(true);
                    holder.llMCQFrame.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (q.isMCQSingle()) {
            holder.llMCQFrame.setVisibility(View.VISIBLE);
            holder.mcqGroupShow.setSingleSelection(true);
            if(q.getMcqOptionsList()==null) {
                q.setNumMCQOptions(0);
                q.setMcqOptionsList(new ArrayList<>());
            }
            if (q.getMcqOptionsList().size()==0) {
                q.setNumMCQOptions(0);
            }
            holder.mcqGroupShow.removeAllViews();
            for (int i = 0; i < questions.get(position).getNumMCQOptions(); i++) {
                Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option_create,holder.mcqGroupShow, false);
                chip.setText(questions.get(position).getMcqOptionsList().get(i));
                chip.setChecked(false);
                holder.mcqGroupShow.addView(chip);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.mcqGroupShow.removeView(v);
                        q.setNumMCQOptions(q.getNumMCQOptions()-1);
                        Chip chip1 = (Chip) v;
                        q.getMcqOptionsList().remove(chip1.getText().toString());
                    }
                });
            }
            holder.btnAddOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = holder.enterOption.getText().toString().trim();
                    Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option_create,holder.mcqGroupShow, false);
                    chip.setText(s);
                    chip.setChecked(false);
                    holder.mcqGroupShow.addView(chip);
                    q.getMcqOptionsList().add(s);
                    q.setNumMCQOptions(q.getNumMCQOptions()+1);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.mcqGroupShow.removeView(v);
                            q.setNumMCQOptions(q.getNumMCQOptions()-1);
                            Chip chip1 = (Chip) v;
                            q.getMcqOptionsList().remove(chip1.getText().toString());
                        }
                    });
                    holder.enterOption.setText("");
                }
            });

        }

        if (q.isMCQMultiple()) {
            holder.llMCQFrame.setVisibility(View.VISIBLE);
            holder.mcqGroupShow.setSingleSelection(false);
            if(q.getMcqOptionsList()==null) {
                q.setNumMCQOptions(0);
                q.setMcqOptionsList(new ArrayList<>());
            }
            if (q.getMcqOptionsList().size()==0) {
                q.setNumMCQOptions(0);
            }
            holder.mcqGroupShow.removeAllViews();
            for (int i = 0; i < questions.get(position).getNumMCQOptions(); i++) {
                Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option_create,holder.mcqGroupShow, false);
                chip.setText(questions.get(position).getMcqOptionsList().get(i));
                chip.setChecked(false);
                holder.mcqGroupShow.addView(chip);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.mcqGroupShow.removeView(v);
                        q.setNumMCQOptions(q.getNumMCQOptions()-1);
                        Chip chip1 = (Chip) v;
                        q.getMcqOptionsList().remove(chip1.getText().toString());
                    }
                });
            }
            holder.btnAddOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = holder.enterOption.getText().toString().trim();
                    Chip chip = (Chip)LayoutInflater.from(context).inflate(R.layout.chip_mcq_option_create,holder.mcqGroupShow, false);
                    chip.setText(s);
                    chip.setChecked(false);
                    holder.mcqGroupShow.addView(chip);
                    q.getMcqOptionsList().add(s);
                    q.setNumMCQOptions(q.getNumMCQOptions()+1);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            holder.mcqGroupShow.removeView(v);
                            q.setNumMCQOptions(q.getNumMCQOptions()-1);
                            Chip chip1 = (Chip) v;
                            q.getMcqOptionsList().remove(chip1.getText().toString());
                        }
                    });
                    holder.enterOption.setText("");
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText questionNumberTvShow;
        EditText fullMarksTvShow;
        EditText optionalTitle;
        ImageButton btnDropDown;
        LinearLayout llEditFrame;
        AppCompatSpinner typeSpinner;
        EditText questionTextTvShow;
        LinearLayout llMCQFrame;
        ChipGroup mcqGroupShow;
        EditText enterOption;
        Button btnAddOption;
        LinearLayout llDirectAnswerFrame;
        EditText correctAnswerText;
        LinearLayout llFileUpload;
        CheckBox checkFileUpEnable;


        public ViewHolder(View itemView) {
            super(itemView);
            questionNumberTvShow = (EditText) itemView.findViewById(R.id.question_number);
            fullMarksTvShow = (EditText) itemView.findViewById(R.id.marks);
            optionalTitle = (EditText) itemView.findViewById(R.id.optional_title);
            btnDropDown = (ImageButton) itemView.findViewById(R.id.drop_down);
            llEditFrame = (LinearLayout) itemView.findViewById(R.id.ll2);
            typeSpinner = (AppCompatSpinner) itemView.findViewById(R.id.spinnerType);
            questionTextTvShow = (EditText) itemView.findViewById(R.id.question_text);
            llMCQFrame = (LinearLayout) itemView.findViewById(R.id.ll_mcq_frame);
            mcqGroupShow = (ChipGroup) itemView.findViewById(R.id.mcq_options);
            enterOption = (EditText) itemView.findViewById(R.id.enter_option);
            btnAddOption = (Button) itemView.findViewById(R.id.btnAddOption);
            llDirectAnswerFrame = (LinearLayout) itemView.findViewById(R.id.ll_direct_answer_frame);
            correctAnswerText = (EditText) itemView.findViewById(R.id.et_correct_answer);
            llFileUpload = (LinearLayout) itemView.findViewById(R.id.ll_file_upload);
            checkFileUpEnable = (CheckBox) itemView.findViewById(R.id.checkFileUploading);

        }
    }
}
