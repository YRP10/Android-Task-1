
package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.todolist.databinding.ActivityDataInsertBinding;

public class activity_data_insert extends AppCompatActivity {
    ActivityDataInsertBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDataInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String type = getIntent().getStringExtra("type");

        if (type != null && type.equals("update")) {
            setTitle("Update");
            binding.Title.setText(getIntent().getStringExtra("title"));
            binding.Disp.setText(getIntent().getStringExtra("disp"));
            int id = getIntent().getIntExtra("id", 0);
            binding.add.setText("Update Note");
            binding.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateNote(id);
                }
            });
        } else {
            setTitle("Add Mode");
            binding.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addNote();
                }
            });
        }
    }

    private void updateNote(int id) {
        Intent intent = new Intent();
        intent.putExtra("title", binding.Title.getText().toString());
        intent.putExtra("disp", binding.Disp.getText().toString());
        intent.putExtra("id", id);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void addNote() {
        Intent intent = new Intent();
        intent.putExtra("title", binding.Title.getText().toString());
        intent.putExtra("disp", binding.Disp.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(activity_data_insert.this, MainActivity.class));
    }
}
